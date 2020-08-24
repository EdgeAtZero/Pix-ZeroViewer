@file:Suppress("DEPRECATION")

package com.edgeatzero.library.ext

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import com.edgeatzero.library.util.FlagsUtils

inline fun <reified T : ViewGroup.LayoutParams> View.safeUpdateLayoutParams(block: T.() -> Unit) {
    val params = layoutParams as? T
    if (params != null) {
        block.invoke(params)
        layoutParams = params
    }
}

fun View.layout(range: Rect) {
    layout(range.left, range.top, range.right, range.bottom)
}

val View.locationOnScreen
    get() = Rect().also { result ->
        val position = IntArray(2)
        getLocationOnScreen(position)
        result.left = position[0]
        result.top = position[1]
        result.right = position[0] + width
        result.bottom = position[1] + height
    }

fun View.addSystemUiVisibilityFlag(flag: Int) {
    systemUiVisibility = FlagsUtils.add(systemUiVisibility, flag)
}

fun View.includeSystemUiVisibilityFlag(flag: Int): Boolean {
    return FlagsUtils.include(systemUiVisibility, flag)
}

fun View.removeSystemUiVisibilityFlag(flag: Int) {
    systemUiVisibility = FlagsUtils.remove(systemUiVisibility, flag)
}
