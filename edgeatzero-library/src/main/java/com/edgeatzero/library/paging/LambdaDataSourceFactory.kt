package com.edgeatzero.library.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.edgeatzero.library.interfaces.ControllerDataProvider
import com.edgeatzero.library.interfaces.ControllerDataHolderInstanceHolder

open class LambdaDataSourceFactory<Key, Value>(
    private val init: () -> Unit = {},
    private val lambda: () -> DataSource<Key, Value>
) : DataSource.Factory<Key, Value>(), ControllerDataHolderInstanceHolder {

    private val _holder = MutableLiveData<ControllerDataProvider>()
    override val provider: LiveData<ControllerDataProvider> = _holder

    private var dataSource: DataSource<Key, Value>? = null

    override fun create(): DataSource<Key, Value> {
        val source = lambda.invoke()
        dataSource = source
        (source as? ControllerDataProvider)?.let { _holder.postValue(it) }
        init.invoke()
        return source
    }

    fun invalidate() {
        dataSource?.invalidate()
    }

}
