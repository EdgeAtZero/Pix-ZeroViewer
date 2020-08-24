package com.edgeatzero.library.ext

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import kotlin.reflect.KClass

val ViewDataBinding.context: Context
    get() = root.context

@MainThread
inline fun <reified T : ViewDataBinding> KClass<out T>.inflate(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
): T = java.getDeclaredMethod(
    "inflate",
    LayoutInflater::class.java,
    ViewGroup::class.java,
    Boolean::class.java
).invoke(null, inflater, parent, attachToParent) as T

@MainThread
@JvmOverloads
inline fun <reified T : ViewDataBinding> LayoutInflater.inflate(
    parent: ViewGroup? = null,
    attachToParent: Boolean = false
): T = T::class.inflate(this, parent, attachToParent)

//@MainThread
//@JvmOverloads
//inline fun <reified T : ViewDataBinding> T.inflate(
//    inflater: LayoutInflater,
//    parent: ViewGroup? = null,
//    attachToParent: Boolean = false
//): T = inflater.inflate(parent, attachToParent)
