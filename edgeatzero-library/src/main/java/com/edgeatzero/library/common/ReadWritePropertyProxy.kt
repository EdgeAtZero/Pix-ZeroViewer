package com.edgeatzero.library.common

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ReadWritePropertyProxy<P0, P1>(
    private val initializer: () -> ReadWriteProperty<P0, P1>
) : ReadWriteProperty<P0, P1> {

    private var cache: ReadWriteProperty<P0, P1>? = null

    private val property: ReadWriteProperty<P0, P1>
        get() = cache ?: initializer.invoke().also { cache = it }

    override fun getValue(thisRef: P0, property: KProperty<*>): P1 {
        return this.property.getValue(thisRef, property)
    }

    override fun setValue(thisRef: P0, property: KProperty<*>, value: P1) {
        return this.property.setValue(thisRef, property, value)
    }

    public fun isInitialized(): Boolean {
        return cache != null
    }

}
