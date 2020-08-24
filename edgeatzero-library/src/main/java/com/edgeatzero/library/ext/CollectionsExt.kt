package com.edgeatzero.library.ext

/**
 * 是否包含另一个 Iterable 的任何值
 */
fun <T> Iterable<T>.contains(iterable: Iterable<T>): Boolean {
    return any { iterable.contains(it) }
}
