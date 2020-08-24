package com.edgeatzero.library.model

data class Params<Key, Value>(val key: Key?, val callback: (LoadResult<Key, Value>) -> Unit)
