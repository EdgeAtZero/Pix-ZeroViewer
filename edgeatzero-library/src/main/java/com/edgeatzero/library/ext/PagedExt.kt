@file:SuppressLint("RestrictedApi")

package com.edgeatzero.library.ext

import android.annotation.SuppressLint
import androidx.annotation.AnyThread
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.switchMap
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.edgeatzero.library.model.Controller
import com.edgeatzero.library.paging.LambdaDataSourceFactory
import java.util.concurrent.Executor

@JvmOverloads
fun <Key, Value> newController(
    config: PagedList.Config,
    initialLoadKey: Key? = null,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor(),
    factory: () -> DataSource<Key, Value>
): Controller<Value> = LambdaDataSourceFactory(lambda = factory).toController(
    config,
    initialLoadKey,
    boundaryCallback,
    fetchExecutor
)

@AnyThread
@JvmOverloads
fun <Key, Value> LambdaDataSourceFactory<Key, Value>.toController(
    config: PagedList.Config,
    initialLoadKey: Key? = null,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    fetchExecutor: Executor = ArchTaskExecutor.getIOThreadExecutor()
): Controller<Value> = with(LivePagedListBuilder(this, config)) {
    setInitialLoadKey(initialLoadKey)
    setBoundaryCallback(boundaryCallback)
    setFetchExecutor(fetchExecutor)
    Controller(
        data = build(),
        state = { provider.switchMap { it.state } },
        retry = { fetchExecutor.execute { provider.value?.retry() } },
        refresh = { fetchExecutor.execute { invalidate() } }
    )
}

class SingleTimeLambda<T>(
    private val lambda: () -> T
) : () -> T {

    private var invoked = false

    override fun invoke(): T {
        check(!invoked)
        return lambda.invoke()
    }

}
