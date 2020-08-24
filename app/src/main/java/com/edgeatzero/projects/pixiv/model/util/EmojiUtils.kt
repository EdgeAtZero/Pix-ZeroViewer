package com.edgeatzero.projects.pixiv.model.util

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.style.ImageSpan
import androidx.annotation.WorkerThread
import androidx.core.text.toSpannable
import com.edgeatzero.library.ext.get
import com.edgeatzero.library.ext.logd
import com.edgeatzero.library.ext.range
import com.edgeatzero.library.ext.set
import com.edgeatzero.library.util.Utils
import com.edgeatzero.projects.pixiv.BuildConfig
import com.edgeatzero.projects.pixiv.database.EmojiDao
import com.edgeatzero.projects.pixiv.database.EmojiEntity
import com.edgeatzero.projects.pixiv.http.service.PixivApplicationApiService
import com.edgeatzero.projects.pixiv.http.suspendExecute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.InputStream
import java.util.regex.Matcher

object EmojiUtils : KodeinAware {

    override val kodein = Utils.kodein

    private val emojiDao by kodein.instance<EmojiDao>()

    private val client by kodein.instance<OkHttpClient>("Glide")

    private val applicationApiService by kodein.instance<PixivApplicationApiService>()

    private lateinit var cache: List<EmojiEntity>

    val items: List<EmojiEntity>?
        get() = if (::cache.isInitialized) cache else null

    private var emoji: List<EmojiEntity>
        @WorkerThread
        set(value) {
            cache = value
            emojiDao.insert(value)
        }
        @WorkerThread
        get() {
            val queryAll = emojiDao.queryAll()
            cache = queryAll
            return queryAll
        }

    fun transform(source: CharSequence, check: Boolean = false) : Spannable {
        return transform(source.toSpannable(), check)
    }

    fun <T : Spannable> transform(source: T, check: Boolean = false): T {
        for (entity in cache) {
            val matcher = entity.definition.pattern.matcher(source)
            loop@ while (matcher.find()) {
                val range = matcher.range()
                if (check && source.get<ImageSpan>(range).isNotEmpty()) continue@loop
                load(source, range, entity.stream, matcher)
            }
        }
        return source
    }

    fun <T : Spannable> load(
        source: T,
        range: IntRange = IntRange(0, source.length),
        stream: InputStream,
        matcher: Matcher? = null
    ): T {
        val text = matcher?.group() ?: source.substring(range.first, range.last)
        if (BuildConfig.DEBUG) logd("fun load --> source $text, range: $range, text: $source")
        try {
            val drawable = Drawable.createFromStream(stream, null)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            source[range] = ImageSpan(drawable, text)
        } catch (e: Exception) {
        }
        return source
    }

    val size: Int
        @WorkerThread
        get() = emojiDao.queryCount()

    suspend fun init() {
        if (size == 0) update()
        else cache = emoji
    }

    suspend fun update() {
        withContext(Dispatchers.IO) {
            applicationApiService.emoji().suspendExecute(
                onSuccessful = { body ->
                    val emoji = ArrayList<EmojiEntity>()
                    body.emojiDefinitions.forEach {
                        try {
                            val request = Request.Builder().url(it.imageUrlMedium).build()
                            val response = client.newCall(request).execute()
                            val entity = EmojiEntity(it, response.body!!.bytes())
                            emoji.add(entity)
                        } catch (e: Exception) {
                        }
                    }
                    this@EmojiUtils.emoji = emoji
                }, onFailed = { _, _ ->
                }
            )
        }
    }

}
