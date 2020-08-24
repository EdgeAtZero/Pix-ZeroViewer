package com.edgeatzero.library.common

import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class WeakProperty<T : Any>(
    initial: () -> T? = { null }
) : ReadWriteProperty<Any, T?> {

    private var reference = WeakReference(initial())

    override fun getValue(thisRef: Any, property: KProperty<*>): T? = reference.get()

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        reference = WeakReference(value)
    }

    fun clear() {
         reference.clear()
    }

}
