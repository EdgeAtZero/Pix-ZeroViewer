package com.edgeatzero.library.common

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ReadOnlyPropertyProxy<P0, P1>(
    private val initializer: () -> ReadOnlyProperty<P0, P1>
) : ReadOnlyProperty<P0, P1> {

    private var cache: ReadOnlyProperty<P0, P1>? = null

    private val property: ReadOnlyProperty<P0, P1>
        get() = cache ?: initializer.invoke().also { cache = it }

    override fun getValue(thisRef: P0, property: KProperty<*>): P1 {
        return this.property.getValue(thisRef, property)
    }

    public fun isInitialized(): Boolean {
        return cache != null
    }

}
