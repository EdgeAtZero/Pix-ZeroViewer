package com.edgeatzero.library.common

open class SingletonHolderDoubleArg<out T, in A, in B>(private val creator: (A, B) -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(arg1: A, arg2: B): T = instance ?: synchronized(this) {
        instance ?: creator(arg1, arg2).apply { instance = this }
    }

}
