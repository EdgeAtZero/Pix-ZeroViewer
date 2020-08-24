package com.edgeatzero.library.ext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible

val Fragment.bundleProducer: () -> Bundle
    get() = { arguments ?: Bundle().also { arguments = it } }

@MainThread
inline fun <reified T : Activity> Fragment.startActivity(
    initIntent: Intent.() -> Unit = {}
): Unit = startActivity(Intent(requireActivity(), T::class.java).apply(initIntent))

@MainThread
inline fun <reified T : Activity> Fragment.startActivity(
    initIntent: Intent.() -> Unit = {},
    options: Bundle? = null
): Unit = startActivity(Intent(requireActivity(), T::class.java).apply(initIntent), options)
