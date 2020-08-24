package com.edgeatzero.projects.pixiv.http.okhttp

import android.os.Build
import com.edgeatzero.projects.pixiv.constant.ClientConstant.HASH_SECRET
import com.edgeatzero.projects.pixiv.constant.ClientConstant.clientTimeFormat
import com.edgeatzero.projects.pixiv.util.Settings
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

class PixivHeadersInterceptor : Interceptor {

    private val locale
        get() = Settings.locale

    private val format = SimpleDateFormat(clientTimeFormat, locale)

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request().let { originalRequest ->
            originalRequest.newBuilder().apply {
                headers(originalRequest.headers.let { originalRequestHeaders ->

                    Headers.Builder().apply {
                        for (i in 0 until originalRequestHeaders.size) {
                            val name = originalRequestHeaders.name(i)
                            var value = originalRequestHeaders.value(i)
                            if (name == "Authorization") {
                                if (value.isNotBlank()) {
                                    if (!value.startsWith("Bearer ")) value = "Bearer $value"
                                    add(name, value)
                                }
                            } else add(name, value)
                        }
                        add("Accept-Language: ${locale.language}_${locale.country}")
                        add("App-OS: Android")
                        add("App-OS-Version: 9")
                        add("App-Version: 5.0.206")
                        add("referer: https://app-api.pixiv.net/")
                        add("User-Agent: PixivAndroidApp/5.0.204 (Android ${Build.VERSION.RELEASE}; ${Build.MODEL})")
                        val date = format.format(Date())
                        add("X-Client-Time: $date")
                        add("X-Client-Hash: ${md5("${date}$HASH_SECRET")}")
                    }
                }.build())
            }.build()
        })

    private fun md5(s: String): String? = try {
        val bytes = MessageDigest.getInstance("MD5").also { it.update(s.toByteArray()) }.digest()
        buildString {
            var i: Int
            for (offset in bytes.indices) {
                i = bytes[offset].toInt()
                if (i < 0) i += 256
                if (i < 16) append('0')
                append(Integer.toHexString(i))
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

}
