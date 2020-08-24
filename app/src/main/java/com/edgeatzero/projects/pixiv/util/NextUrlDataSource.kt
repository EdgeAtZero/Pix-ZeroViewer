package com.edgeatzero.projects.pixiv.util

import com.edgeatzero.library.ext.fromJson
import com.edgeatzero.library.ext.fromJsonOrNull
import com.edgeatzero.library.ext.tryGetClient
import com.edgeatzero.library.model.LoadResult
import com.edgeatzero.library.model.Params
import com.edgeatzero.library.paging.BasePageKeyedDataSource
import com.edgeatzero.library.util.HttpMessageConstant
import com.edgeatzero.projects.pixiv.model.FailedData
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call

abstract class NextUrlDataSource<Response : Any, Data : Any>(
    private val client: OkHttpClient
) : BasePageKeyedDataSource<String, Data>() {

    override fun load(params: Params<String, Data>) {
        val key = params.key
        val call = prepareCall()
        val request = if (key != null) call.request().newBuilder().url(key).build()
        else call.request()
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.let { processResponseBody(it) }
                if (body != null) params.callback.invoke(processResponse(body))
                else params.callback.invoke(LoadResult.Failed(HttpMessageConstant.message_data_is_empty))
            } else {
                val s = response.body?.string().fromJsonOrNull<FailedData>()?.errorMessage
                params.callback.invoke(LoadResult.Failed(s ?: response.message))
            }
            response.close()
        } catch (e: Exception) {
            params.callback.invoke(LoadResult.Failed(e.message))
        }
    }

    abstract fun prepareCall(): Call<Response>

    abstract fun processResponse(response: Response): LoadResult.Successful<String, Data>

    abstract fun processResponseBody(body: ResponseBody): Response

}

inline fun <reified Response : Any, reified Data : Any> OkHttpClient.nextUrlDataSource(
    noinline prepareCall: () -> Call<Response>,
    noinline processResponse: (response: Response) -> LoadResult.Successful<String, Data>,
    noinline processResponseBody: (body: ResponseBody) -> Response = { it.string().fromJson() }
) = object : NextUrlDataSource<Response, Data>(this) {

    override fun prepareCall(): Call<Response> {
        return prepareCall.invoke()
    }

    override fun processResponse(response: Response): LoadResult.Successful<String, Data> {
        return processResponse.invoke(response)
    }

    override fun processResponseBody(body: ResponseBody): Response {
        return processResponseBody.invoke(body)
    }

}
