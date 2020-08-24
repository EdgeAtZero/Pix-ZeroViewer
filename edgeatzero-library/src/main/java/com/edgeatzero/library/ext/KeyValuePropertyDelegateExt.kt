package com.edgeatzero.library.ext

import com.edgeatzero.library.common.KeyValuePropertyDelegate

fun <T, V> T.delegate(
    key: String? = null,
    getter: T.(key: String) -> V,
    setter: T.(key: String, value: V) -> Unit = { _, _ -> }
) = KeyValuePropertyDelegate(source = this, key = key, getter = getter, setter = setter)

fun <T, V> (() -> T).invokeDelegate(
    key: String? = null,
    getter: T.(key: String) -> V,
    setter: T.(key: String, value: V) -> Unit = { _, _ -> }
) = KeyValuePropertyDelegate(
    source = this,
    key = key,
    getter = { invoke().getter(it) },
    setter = { p0, p1 -> invoke().setter(p0, p1) }
)
