package com.edgeatzero.library.common.drawable

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat

class FadingDrawable(
    display: Drawable,
    toggle: Drawable
) : LayerDrawable(arrayOf(display, toggle)) {

    constructor(context: Context, @DrawableRes display: Int, @DrawableRes toggle: Int) : this(
        requireNotNull(ContextCompat.getDrawable(context, display)),
        requireNotNull(ContextCompat.getDrawable(context, toggle))
    )

    @FloatRange(from = 0.0, to = 1.0)
    var fading: Float = 1F
        set(value) {
            field = value
            getDrawable(0).alpha = 225.times(value).toInt()
            getDrawable(1).alpha = 255.times(1.minus(value)).toInt()
            invalidateSelf()
        }

    init {
        getDrawable(0).alpha = 255
        getDrawable(1).alpha = 0
    }

}
