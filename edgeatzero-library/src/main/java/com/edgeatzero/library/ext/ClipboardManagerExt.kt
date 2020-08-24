package com.edgeatzero.library.ext

import android.content.ClipData
import android.view.View
import android.view.ViewGroup

inline operator fun ClipData.get(index: Int): ClipData.Item? {
    return getItemAt(index)
}

inline fun ClipData.forEach(action: (item: ClipData.Item) -> Unit) {
    for (index in 0 until itemCount) {
        action.invoke(getItemAt(index))
    }
}
