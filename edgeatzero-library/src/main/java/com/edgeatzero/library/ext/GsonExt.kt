package com.edgeatzero.library.ext

import androidx.annotation.AnyThread
import com.edgeatzero.library.util.GsonHolder

@AnyThread
inline fun <reified T : Any> T.toJson(
): String = GsonHolder.INSTANCE.toJson(this)

@AnyThread
inline fun <reified T : Any> T?.toJsonOrNull(
): String? = if (this != null) try {
    GsonHolder.INSTANCE.toJson(this)
} catch (e: Exception) {
    e.printStackTrace()
    null
} else null

@AnyThread
inline fun <reified T : Any> String.fromJson(
): T = GsonHolder.INSTANCE.fromJson(this, T::class.java)

@AnyThread
inline fun <reified T : Any> String?.fromJsonOrNull(
): T? = if (this != null) try {
    GsonHolder.INSTANCE.fromJson(this, T::class.java)
} catch (e: Exception) {
    e.printStackTrace()
    null
} else null
