package com.edgeatzero.library.common

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class KeyValuePropertyDelegate<T, V>(
    private val source: T,
    private val key: String? = null,
    private val getter: T.(key: String) -> V,
    private val setter: T.(key: String, value: V) -> Unit
) : ReadWriteProperty<Any, V> {

    override fun getValue(thisRef: Any, property: KProperty<*>): V {
        return source.getter(key ?: property.name)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: V) {
        source.setter(key ?: property.name, value)
    }

}
