package com.edgeatzero.library.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ViewModelProperty<VM : ViewModel>(
    private val kClass: KClass<VM>,
    private val store: () -> ViewModelStore,
    private val factory: () -> ViewModelProvider.Factory
) : ReadWriteProperty<Any, VM> {

    private var cache: VM? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): VM {
        return cache ?: ViewModelProvider(store.invoke(), factory.invoke()).get(kClass.java).also {
            cache = it
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: VM) {
        cache = value
    }

}
