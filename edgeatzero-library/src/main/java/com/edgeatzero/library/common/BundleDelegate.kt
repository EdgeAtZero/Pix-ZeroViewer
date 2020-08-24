package com.edgeatzero.library.common

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class BundleDelegate<T>(
    private val bundle: () -> Bundle,
    private val key: String? = null,
    private val defaultValue: T,
    private val getter: Bundle.(String, T) -> T,
    private val setter: Bundle.(String, T) -> Unit
) : ReadWriteProperty<Any, T> {

    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): T = bundle.invoke().getter(key ?: property.name, defaultValue)

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: T
    ): Unit = bundle.invoke().setter(key ?: property.name, value)

    companion object {

        @JvmStatic
        fun <T : Any> (() -> Bundle).notnull(
            key: String? = null,
            defaultValue: T,
            getter: Bundle.(String, T) -> T,
            setter: Bundle.(String, T) -> Unit
        ) = BundleDelegate(
            bundle = this,
            key = key,
            defaultValue = defaultValue,
            getter = getter,
            setter = setter
        )

        @JvmStatic
        fun <T : Any> (() -> Bundle).nullable(
            key: String? = null,
            defaultValue: T? = null,
            getter: Bundle.(String, T?) -> T?,
            setter: Bundle.(String, T) -> Unit
        ) = BundleDelegate(
            bundle = this,
            key = key,
            defaultValue = defaultValue,
            getter = getter,
            setter = { p0, p1 -> p1?.let { setter(p0, p1) } }
        )

        @JvmStatic
        fun <T : Any> Bundle.notnull(
            key: String? = null,
            defaultValue: T,
            getter: Bundle.(String, T) -> T,
            setter: Bundle.(String, T) -> Unit
        ) = BundleDelegate(
            bundle = { this },
            key = key,
            defaultValue = defaultValue,
            getter = getter,
            setter = setter
        )

        @JvmStatic
        fun <T : Any> Bundle.nullable(
            key: String? = null,
            defaultValue: T? = null,
            getter: Bundle.(String, T?) -> T?,
            setter: Bundle.(String, T) -> Unit
        ) = BundleDelegate(
            bundle = { this },
            key = key,
            defaultValue = defaultValue,
            getter = getter,
            setter = { p0, p1 -> p1?.let { setter(p0, p1) } }
        )

    }

}
