package com.edgeatzero.library.common

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferencesDelegate<T>(
    private val sharedPreferences: () -> SharedPreferences,
    private val key: String? = null,
    private val defaultValue: T,
    private val getter: SharedPreferences.(key: String, defValue: T) -> T,
    private val setter: SharedPreferences.Editor.(key: String, value: T) -> SharedPreferences.Editor
) : ReadWriteProperty<Any, T> {

    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ): T = sharedPreferences.invoke().getter(key ?: property.name, defaultValue)

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: T
    ): Unit = sharedPreferences.invoke().edit().setter(key ?: property.name, value).apply()

    companion object {

        @JvmStatic
        fun <T : Any> (() -> SharedPreferences).notnull(
            key: String? = null,
            defaultValue: T,
            getter: SharedPreferences.(key: String, defValue: T) -> T,
            setter: SharedPreferences.Editor.(key: String, value: T) -> SharedPreferences.Editor
        ) = SharedPreferencesDelegate(
            sharedPreferences = this,
            key = key,
            defaultValue = defaultValue,
            getter = getter,
            setter = setter
        )

        @JvmStatic
        fun <T : Any> (() -> SharedPreferences).nullable(
            key: String? = null,
            defaultValue: T? = null,
            getter: SharedPreferences.(key: String, defValue: T?) -> T?,
            setter: SharedPreferences.Editor.(key: String, value: T) -> SharedPreferences.Editor
        ) = SharedPreferencesDelegate(
            sharedPreferences = this,
            key = key,
            defaultValue = defaultValue,
            getter = getter,
            setter = { p0, p1 -> p1?.let { setter(p0, p1) } ?: this }
        )


        @JvmStatic
        fun <T : Any> SharedPreferences.notnull(
            key: String? = null,
            defaultValue: T,
            getter: SharedPreferences.(key: String, defValue: T) -> T,
            setter: SharedPreferences.Editor.(key: String, value: T) -> SharedPreferences.Editor
        ) = SharedPreferencesDelegate(
            sharedPreferences = { this },
            key = key,
            defaultValue = defaultValue,
            getter = getter,
            setter = setter
        )

        @JvmStatic
        fun <T : Any> SharedPreferences.nullable(
            key: String? = null,
            defaultValue: T? = null,
            getter: SharedPreferences.(String, T?) -> T?,
            setter: SharedPreferences.Editor.(key: String, value: T) -> SharedPreferences.Editor
        ) = SharedPreferencesDelegate(
            sharedPreferences = { this },
            key = key,
            defaultValue = defaultValue,
            getter = getter,
            setter = { p0, p1 -> p1?.let { setter(p0, p1) } ?: this }
        )

    }

}
