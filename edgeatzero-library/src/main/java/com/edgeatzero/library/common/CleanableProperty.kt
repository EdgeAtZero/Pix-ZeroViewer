package com.edgeatzero.library.common

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CleanableProperty<T : Any>(
    private val creator: () -> T
) : ReadOnlyProperty<Any, T> {

    private var value by WeakProperty<T>()

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value ?: creator.invoke().also { value = it }
    }

    fun clear() {
        value = null
    }

}
