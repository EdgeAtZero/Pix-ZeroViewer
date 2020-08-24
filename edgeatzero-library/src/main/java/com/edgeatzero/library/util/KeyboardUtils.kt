package com.edgeatzero.library.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService

object KeyboardUtils {

//    val sharedPreferences by lazy {
//        Utils.context.getSharedPreferences("keyboard", Context.MODE_PRIVATE)
//    }

    fun isActive(context: Context = Utils.context): Boolean {
        val manager = context.getSystemService<InputMethodManager>() ?: return false
        return manager.isActive
    }

    fun isActive(view: View): Boolean {
        val manager = view.context.getSystemService<InputMethodManager>() ?: return false
        return manager.isActive(view)
    }

    fun showSoftInput(view: View, flag: Int = 0): Boolean {
        val manager = view.context.getSystemService<InputMethodManager>() ?: return false
        view.requestFocus()
        return manager.showSoftInput(view, flag)
    }

    fun hideSoftInputFromWindow(view: View, flag: Int = 0): Boolean {
        val manager = view.context.getSystemService<InputMethodManager>() ?: return false
        return manager.hideSoftInputFromWindow(view.windowToken, flag)
    }

    fun hideSoftInputFromWindow(activity: Activity, flag: Int = 0): Boolean {
        val manager = activity.getSystemService<InputMethodManager>() ?: return false
        return manager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, flag)
    }

//    var height by sharedPreferences.delegate(
//        key = "height",
//        getter = { getInt(it, 0) },
//        setter = { key, value -> edit().putInt(key, value).apply() }
//    )

}
