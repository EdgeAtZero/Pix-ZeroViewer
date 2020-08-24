package com.edgeatzero.library.ext

import android.view.Window
import android.view.WindowManager

fun Window.updateAttributes(block: WindowManager.LayoutParams.() -> Unit) {
    val attributes = attributes
    block.invoke(attributes)
    this.attributes = attributes
}

fun WindowManager.LayoutParams.addFlags(flags: Int) {
    setFlags(flags, flags)
}

fun WindowManager.LayoutParams.clearFlags(flags: Int) {
    setFlags(0, flags)
}

fun WindowManager.LayoutParams.setFlags(flags: Int, mask: Int) {
    this.flags = this.flags and mask.inv() or (flags and mask)
}
