package com.edgeatzero.projects.pixiv

import com.edgeatzero.library.util.HttpMessageConstant.message_data_is_empty
import com.edgeatzero.library.util.HttpMessageConstant.message_unknown_error
import com.edgeatzero.projects.pixiv.model.FailedData
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.create
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy

private val server = with(Retrofit.Builder()) {
    baseUrl(TestPixivApplicationApiService.baseUrl)
    client(with(OkHttpClient.Builder()) {
        proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(InetAddress.getByName("127.0.0.1"), 1080)))
    }.build())
    build()
}.create<TestPixivApplicationApiService>()

fun main() {
    val call = server.emoji()
//    val call = server.novelBookmarkDetail(authorization = "Bearer kZxu4q5o0TL_xfcRMjOUhPi-FrYJq2MM8AI-aQZpyeU", illust_id = 13380766)
    call.execute(
        onSuccessful = { body ->
            println(body.string())
        },
        onFailed = { _, body ->
            println(body?.errorMessage)
        }
    )
}

inline fun <T> Call<T>.execute(
    crossinline onSuccessful: (body: T) -> Unit,
    crossinline onFailed: (message: String, body: FailedData?) -> Unit
) {
    try {
        val response = execute()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                onSuccessful.invoke(body)
            } else {
                onFailed.invoke(message_data_is_empty, null)
            }
        } else {
            onFailed.invoke(
                response.message(),
                response.errorBody()?.string()?.fromJsonOrNull<FailedData>()
            )
        }
    } catch (e: Exception) {
        onFailed.invoke(e.message ?: message_unknown_error, null)
    }
}

inline fun <reified T> String.fromJsonOrNull(): T? = try {
    Gson().fromJson(this, T::class.java)
} catch (e: Exception) {
    e.printStackTrace()
    null
}
