package com.edgeatzero.library.common.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class RotatingDrawable(layers: Array<out Drawable>) : LayerDrawable(layers) {

    constructor(target: Drawable) : this(arrayOf(target))

    constructor(
        context: Context,
        @DrawableRes target: Int
    ) : this(ContextCompat.getDrawable(context, target)?.let { arrayOf(it) } ?: emptyArray())

    var rotation: Float = 0f
        set(value) {
            field = value
            invalidateSelf()
        }

    operator fun get(index: Int): Drawable? {
        return try {
            getDrawable(index)
        } catch (e: Exception) {
            null
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.rotate(rotation, bounds.centerX().toFloat(), bounds.centerY().toFloat())
        super.draw(canvas)
        canvas.restore()
    }

}
