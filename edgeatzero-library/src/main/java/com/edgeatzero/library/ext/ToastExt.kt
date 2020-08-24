package com.edgeatzero.library.ext

import android.content.Context
import android.widget.Toast
import androidx.annotation.MainThread

@MainThread
fun Context.toast(value: Any?) = toast { value }

@MainThread
inline fun Context.toast(
    value: Context.() -> Any?
): Unit = Toast.makeText(this, value.invoke(this).toString(), Toast.LENGTH_SHORT).show()
