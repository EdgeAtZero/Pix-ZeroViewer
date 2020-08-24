package com.edgeatzero.library.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@JvmOverloads
@OptIn(ExperimentalContracts::class)
fun <T> CoroutineScope.launchAsLiveData(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    initial: T? = null,
    block: suspend CoroutineScope.(data: MutableLiveData<T>) -> Unit
): LiveData<T> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    val data = MutableLiveData<T>()
    if (initial != null) {
        data.value = initial
    }
    launch(context, start) {
        block.invoke(this, data)
    }
    return data
}
