package com.edgeatzero.library.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.edgeatzero.library.interfaces.ControllerDataProvider
import com.edgeatzero.library.model.LoadResult
import com.edgeatzero.library.model.LoadState
import com.edgeatzero.library.model.Params


abstract class BasePageKeyedDataSource<Key, Value> : PageKeyedDataSource<Key, Value>(),
    ControllerDataProvider {

    protected val _state = MutableLiveData<LoadState>()
    override val state: LiveData<LoadState> = _state

    protected var _retry: (() -> Unit)? = null

    override fun retry() {
        val retry = _retry
        _retry = null
        retry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Key>,
        callback: LoadInitialCallback<Key, Value>
    ) {
        _state.postValue(LoadState.INITIAL_LOADING)
        load(Params(null) {
            when (it) {
                is LoadResult.Successful -> {
                    _state.postValue(if (it.data.isEmpty()) LoadState.EMPTY else if (it.nextKey == null) LoadState.COMPLETED else LoadState.LOADED)
                    callback.onResult(it.data, it.previous, it.nextKey)
                }
                is LoadResult.Failed -> {
                    _retry = { loadInitial(params, callback) }
                    _state.postValue(LoadState.error(it.message ?: "Unknown error(s) happened!"))
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        _state.postValue(LoadState.LOADING)
        load(Params(params.key) {
            when (it) {
                is LoadResult.Successful -> {
                    _state.postValue(if (it.nextKey == null) LoadState.COMPLETED else LoadState.LOADED)
                    callback.onResult(it.data, it.nextKey)
                }
                is LoadResult.Failed -> {
                    _retry = { loadAfter(params, callback) }
                    _state.postValue(LoadState.error(it.message ?: "Unknown error(s) happened!"))
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        _state.postValue(LoadState.LOADING)
        load(Params(params.key) {
            when (it) {
                is LoadResult.Successful -> {
                    _state.postValue(if (it.nextKey == null) LoadState.COMPLETED else LoadState.LOADED)
                    callback.onResult(it.data, it.previous)
                }
                is LoadResult.Failed -> {
                    _retry = { loadBefore(params, callback) }
                    _state.postValue(LoadState.error(it.message ?: "Unknown error(s) happened!"))
                }
            }
        })
    }

    abstract fun load(params: Params<Key, Value>)

}
