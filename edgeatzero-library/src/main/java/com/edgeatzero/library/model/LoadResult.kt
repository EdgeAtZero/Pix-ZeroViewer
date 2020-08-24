package com.edgeatzero.library.model

sealed class LoadResult<Key, Value> {

    data class Successful<Key, Value>(
        val data: List<Value>,
        val previous: Key? = null,
        val nextKey: Key? = null
    ) : LoadResult<Key, Value>()

    data class Failed<Key, Value>(
        val message: String?
    ) : LoadResult<Key, Value>()

}
