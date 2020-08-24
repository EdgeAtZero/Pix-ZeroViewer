package com.edgeatzero.library.ext

import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

@MainThread
inline fun <T : Any, R : Any> LiveData<T?>.nullableSwitchMap(
    crossinline transform: (T) -> LiveData<R>
): LiveData<R?> {
    val result = MediatorLiveData<R?>()
    result.addSource(this, object : Observer<T?> {

        private var last: LiveData<R>? = null

        override fun onChanged(t: T?) {
            if (t == null) result.value = null
            else {
                val v = transform.invoke(t)
                last?.let { result.removeSource(it) }
                result.addSource(v) {
                    result.value = it
                }
                last = v
            }
        }

    })
    return result
}

@MainThread
inline fun <T : Any, R : Any> LiveData<T?>.notNullSwitchMap(
    crossinline transform: (T) -> LiveData<R>
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this, object : Observer<T?> {

        private var last: LiveData<R>? = null

        override fun onChanged(t: T?) {
            if (t != null) {
                val v = transform.invoke(t)
                last?.let { result.removeSource(it) }
                result.addSource(v) {
                    result.value = it
                }
                last = v
            }
        }

    })
    return result
}

@MainThread
fun <T> MutableLiveData<T>.setIfDifferent(item: T?) {
    if (value != item) value = item
}

@AnyThread
fun <T> MutableLiveData<T>.postIfDifferent(item: T) {
    if (value != item) postValue(item)
}

@MainThread
fun <T> MutableLiveData<T>.setNext(map: (T?) -> T) {
    value = map(value)
}

@AnyThread
fun <T> MutableLiveData<T>.postNext(map: (T?) -> T) {
    postValue(map(value))
}
