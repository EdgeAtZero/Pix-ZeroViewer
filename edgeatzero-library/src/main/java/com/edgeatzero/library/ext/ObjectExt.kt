@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "NOTHING_TO_INLINE")

package com.edgeatzero.library.ext

inline fun Any.wait(): Unit = (this as Object).wait()

inline fun Any.wait(timeout: Long): Unit = (this as Object).wait(timeout)

inline fun Any.wait(timeout: Long, nanos: Int): Unit = (this as Object).wait(timeout, nanos)

inline fun Any.notify(): Unit = (this as Object).notify()

inline fun Any.notifyAll(): Unit = (this as Object).notifyAll()
