package com.edgeatzero.library.ext

import android.text.Editable

fun Editable.delete(range: IntRange) {
    delete(range.first, range.last)
}
