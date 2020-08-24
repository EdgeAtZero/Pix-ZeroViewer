package com.edgeatzero.library.ext

import android.graphics.Rect

inline var Rect.width: Int
    get() = width()
    set(value) {
        right += value - width
    }

inline var Rect.height: Int
    get() = height()
    set(value) {
        bottom += value - height
    }
