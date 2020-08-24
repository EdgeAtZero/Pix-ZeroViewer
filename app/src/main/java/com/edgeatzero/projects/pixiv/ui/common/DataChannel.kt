package com.edgeatzero.projects.pixiv.ui.common

import java.lang.ref.WeakReference
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.safeCast

object DataChannel {

    private val data by lazy { WeakHashMap<String, WeakReference<out Any>>() }

    operator fun <T : Any> set(uuid: String?, value: T) {
        data[uuid] = WeakReference(value)
    }

    operator fun <T : Any> get(uuid: String?, kClass: KClass<T>): T? {
        return kClass.safeCast(data[uuid]?.get())
    }

    inline operator fun <reified T : Any> get(uuid: String?): T? {
        return get(uuid, T::class)
    }

    fun clear() {
        data.clear()
    }

}
