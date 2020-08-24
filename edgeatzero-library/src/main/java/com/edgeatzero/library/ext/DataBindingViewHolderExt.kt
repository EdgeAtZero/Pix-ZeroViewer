package com.edgeatzero.library.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import com.edgeatzero.library.common.DataBindingViewHolder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@MainThread
inline fun <reified T : ViewDataBinding> ViewGroup.inflateViewHolder(
    parent: ViewGroup = this,
    inflater: LayoutInflater = LayoutInflater.from(parent.context),
    attachToParent: Boolean = false,
    initialCallback: (binding: T) -> Unit = {}
) : DataBindingViewHolder<T> = DataBindingViewHolder(inflater.inflate<T>(
    parent, attachToParent
).also(initialCallback))

@MainThread
@OptIn(ExperimentalContracts::class)
inline fun <T : ViewDataBinding> DataBindingViewHolder<T>.bind(
    lambda: (binding: T) -> Unit
): DataBindingViewHolder<T> {
    contract {
        callsInPlace(lambda, InvocationKind.EXACTLY_ONCE)
    }
    lambda(binding)
    return this
}

@MainThread
@OptIn(ExperimentalContracts::class)
inline fun <reified T : ViewDataBinding> DataBindingViewHolder<*>.safeBind(
    unCheck: Boolean = false,
    lambda: (binding: T) -> Unit
): DataBindingViewHolder<*> {
    contract {
        callsInPlace(lambda, InvocationKind.EXACTLY_ONCE)
    }
    @Suppress("UNCHECKED_CAST")
    if (unCheck) (binding as T).also(lambda)
    else (binding as? T)?.also(lambda)
    return this
}
