package com.edgeatzero.projects.pixiv.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.edgeatzero.library.ext.inflate
import com.google.android.material.navigation.NavigationView

inline fun <reified T : ViewDataBinding> NavigationView.inflateHeaderView(): T {
    val value = this::class.java.getDeclaredField("presenter").also { it.isAccessible = true }
        .get(this)
    val layout = value::class.java.getDeclaredField("headerLayout").also { it.isAccessible = true }
        .get(value) as ViewGroup
    val binding = LayoutInflater.from(context).inflate<T>(layout)
    value::class.java.getDeclaredMethod("addHeaderView", View::class.java)
        .also { it.isAccessible = true }.invoke(value, binding.root)
    return binding
}
