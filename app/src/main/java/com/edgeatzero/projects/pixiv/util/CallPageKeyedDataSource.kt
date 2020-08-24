package com.edgeatzero.projects.pixiv.util

import com.edgeatzero.library.model.LoadResult
import com.edgeatzero.library.model.Params
import com.edgeatzero.library.paging.BasePageKeyedDataSource
import com.edgeatzero.projects.pixiv.http.execute
import retrofit2.Call

abstract class CallPageKeyedDataSource<Response, Key, Value>: BasePageKeyedDataSource<Key, Value>() {

    override fun load(params: Params<Key, Value>) {
        prepareCall(params.key).execute(
            onSuccessful = {
                params.callback.invoke(processResponse(params.key, it))
            }, onFailed = { message, body ->
                params.callback.invoke(LoadResult.Failed(body?.errorMessage ?: message))
            }
        )
    }

    abstract fun prepareCall(key: Key?): Call<Response>

    abstract fun processResponse(key: Key?, response: Response): LoadResult.Successful<Key, Value>

}

class CallPageKeyedDataSourceImpl<Response, Key, Value>(
    private val prepareCall: (key: Key?) -> Call<Response>,
    private val processResponse: (key: Key?, response: Response) -> LoadResult.Successful<Key, Value>
) : CallPageKeyedDataSource<Response, Key, Value>() {

    override fun prepareCall(
        key: Key?
    ): Call<Response> = prepareCall.invoke(key)

    override fun processResponse(
        key: Key?,
        response: Response
    ): LoadResult.Successful<Key, Value> = processResponse.invoke(key, response)

}
