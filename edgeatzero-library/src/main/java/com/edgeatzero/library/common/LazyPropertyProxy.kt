package com.edgeatzero.library.common

class LazyPropertyProxy<T>(
    private val initializer: () -> Lazy<T>
) : Lazy<T> {

    private var cache: Lazy<T>? = null

    private val property: Lazy<T>
        get() = cache ?: initializer.invoke().also { cache = it }

    override val value: T
        get() = initializer.invoke().value

    override fun isInitialized(): Boolean {
        return cache != null
    }

}
