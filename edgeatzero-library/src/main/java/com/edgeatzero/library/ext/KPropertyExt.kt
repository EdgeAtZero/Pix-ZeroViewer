package com.edgeatzero.library.ext

import com.edgeatzero.library.common.WeakProperty
import kotlin.reflect.KProperty0

inline fun <reified T> KProperty0<T>.clear() {
    (getDelegate() as? WeakProperty<*>)?.clear()
}
