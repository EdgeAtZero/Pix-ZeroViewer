package com.edgeatzero.library.util

object FlagsUtils {

    fun include(
        flags: Int,
        target: Int
    ): Boolean = flags and target == 0

    fun add(
        flags: Int,
        target: Int
    ): Int = flags or target

    fun remove(
        flags: Int,
        target: Int
    ): Int = flags and target.inv()

}
