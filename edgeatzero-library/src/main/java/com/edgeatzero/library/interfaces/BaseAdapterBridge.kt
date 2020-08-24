package com.edgeatzero.library.interfaces

interface BaseAdapterBridge<T> : BaseBridge {

    fun current(): List<T>

    fun clone(): List<T>

    operator fun get(position: Int): T?

}
