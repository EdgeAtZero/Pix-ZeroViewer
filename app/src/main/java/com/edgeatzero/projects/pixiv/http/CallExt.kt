@file:Suppress("BlockingMethodInNonBlockingContext")

package com.edgeatzero.projects.pixiv.http

import com.edgeatzero.library.ext.fromJsonOrNull
import com.edgeatzero.library.util.HttpMessageConstant.message_data_is_empty
import com.edgeatzero.library.util.HttpMessageConstant.message_unknown_error
import com.edgeatzero.projects.pixiv.model.FailedData
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import retrofit2.Call
import java.util.*

//inline fun <T> Call<T>.enqueue(
//    crossinline onSuccessful: (body: T) -> Unit,
//    crossinline onFailed: (message: String, body: FailedData?) -> Unit
//) = enqueue(object : Callback<T> {
//
//    override fun onFailure(call: Call<T>, throwable: Throwable) {
//        onFailed.invoke(throwable.message ?: message_unknown_error, null)
//    }
//
//    override fun onResponse(call: Call<T>, response: Response<T>) {
//        if (response.isSuccessful) {
//            val body = response.body()
//            if (body != null) onSuccessful.invoke(body)
//            else onFailed.invoke(message_data_is_empty, null)
//            (response.body() as? ResponseBody)?.close()
//        } else {
//            onFailed.invoke(response.message(), response.errorBody()?.string()?.fromJsonOrNull())
//        }
//        (response.body() as? ResponseBody)?.close()
//        response.errorBody()?.close()
//    }
//
//})

inline fun <T> Call<T>.execute(
    crossinline onSuccessful: (body: T) -> Unit,
    crossinline onFailed: (message: String, body: FailedData?) -> Unit
) = Test.test1(onFailed) ?: try {
    val response = execute()
    if (response.isSuccessful) {
        val body = response.body()
        if (body != null) onSuccessful.invoke(body)
        else onFailed.invoke(message_data_is_empty, null)
    } else {
        onFailed.invoke(response.message(), response.errorBody()?.string()?.fromJsonOrNull())
    }
    (response.body() as? ResponseBody)?.close()
    response.errorBody()?.close()
} catch (exception: Exception) {
    onFailed.invoke(exception.message ?: message_unknown_error, null)
}

suspend inline fun <T> Call<T>.suspendExecute(
    crossinline onSuccessful: suspend (body: T) -> Unit,
    crossinline onFailed: suspend (message: String, body: FailedData?) -> Unit
) = Test.test2(onFailed) ?: try {
    val response = execute()
    if (response.isSuccessful) {
        val body = response.body()
        if (body != null) onSuccessful.invoke(body)
        else onFailed.invoke(message_data_is_empty, null)
    } else {
        onFailed.invoke(response.message(), response.errorBody()?.string()?.fromJsonOrNull())
    }
    (response.body() as? ResponseBody)?.close()
    response.errorBody()?.close()
} catch (exception: Exception) {
    onFailed.invoke(exception.message ?: message_unknown_error, null)
}

suspend inline fun <T, V> Call<T>.suspendSource(
    crossinline onSuccessful: suspend (body: T) -> V,
    crossinline onFailed: suspend (message: String, body: FailedData?) -> V
): V = Test.test2(onFailed) ?: try {
    val response = execute()
    val result = if (response.isSuccessful) {
        val body = response.body()
        if (body != null) onSuccessful.invoke(body)
        else onFailed.invoke(message_data_is_empty, null)
    } else {
        onFailed.invoke(response.message(), response.errorBody()?.string()?.fromJsonOrNull())
    }
    (response.body() as? ResponseBody)?.close()
    response.errorBody()?.close()
    result
} catch (exception: Exception) {
    onFailed.invoke(exception.message ?: message_unknown_error, null)
}

//inline fun <V,T> Call<T>.source(
//    crossinline onSuccessful: (body: T) -> V,
//    crossinline onFailed: (message: String, body: FailedData?) -> V
//): V = try {
//    val response = execute()
//    val result = if (response.isSuccessful) {
//        val body = response.body()
//        if (body != null) {
//            onSuccessful.invoke(body)
//        } else {
//            onFailed.invoke(message_data_is_empty, null)
//        }
//    } else {
//        onFailed.invoke(response.message(), response.errorBody()?.string()?.fromJsonOrNull())
//    }
//    (response.body() as? ResponseBody)?.close()
//    response.errorBody()?.close()
//    result
//} catch (exception: Exception) {
//    onFailed.invoke(exception.message ?: message_unknown_error, null)
//}

object Test {

    val random = Random()

    const val enable: Boolean = false

    inline fun test1(function: (message: String, body: FailedData?) -> Unit): Unit? {
        return if (enable && random.nextBoolean()) {
            Thread.sleep(1000)
            function.invoke("Auto Test Network", null)
        } else {
            null
        }
    }

    suspend inline fun <V> test2(crossinline function: suspend (message: String, body: FailedData?) -> V): V? {
        return if (enable && random.nextBoolean()) {
            delay(1000)
            function.invoke("Auto Test Network", null)
        } else {
            null
        }
    }

}
