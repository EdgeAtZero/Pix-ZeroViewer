package com.edgeatzero.projects.pixiv.ui.ranking

import android.app.Application
import androidx.lifecycle.*
import com.edgeatzero.library.model.Controller
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.constant.RankingCategory
import com.edgeatzero.projects.pixiv.ui.common.MultiPagingViewModel
import com.edgeatzero.projects.pixiv.util.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class RankingViewModel(
    application: Application,
    handle: SavedStateHandle
) : MultiPagingViewModel<RankingCategory, suspend (String?) -> Controller<*>>(application) {

    companion object {

        const val KEY_CONTENT_TYPE = "content_type"
        const val KEY_DATE = "key_date"

        @JvmStatic
        private val format
            get() = SimpleDateFormat("yyyy-MM-dd", Settings.locale)

    }

    public override val controllers by lazy { HashMap<RankingCategory, MutableLiveData<Controller<*>>>() }

    public override val initializes by lazy { HashMap<RankingCategory, suspend (String?) -> Controller<*>>() }

    private val _contentType = handle.getLiveData<ContentType>(KEY_CONTENT_TYPE)
    val contentType: LiveData<ContentType> = _contentType

    private val date = handle.getLiveData<Long>(KEY_DATE)

    val calendar: Calendar?
        get() {
            val l = date.value ?: return null
            val calendar = Calendar.getInstance(Settings.locale)
            calendar.timeInMillis = l
            return calendar
        }

    val format = date.map { Companion.format.format(it) }

    override suspend fun processInitializer(initializer: suspend (String?) -> Controller<*>): Controller<*> {
        return initializer.invoke(format.value)
    }

    fun postContentType(contentType: ContentType) {
        _contentType.value = contentType
    }

    fun postDate(date: Long) {
        this.date.value = date
    }

}
