package com.edgeatzero.library.ext

import android.content.SharedPreferences

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun SharedPreferences.getString(key: String, defaultValue: String? = null): String? {
    return getString(key, defaultValue)
}
