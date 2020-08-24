package com.edgeatzero.library.common.drawable

import android.graphics.*
import android.graphics.drawable.Drawable
import com.edgeatzero.library.common.drawable.shapes.PathShape

class MaskDrawable(
    var target: Drawable?,
    var mask: PathShape
) : Drawable() {

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(mask.path)
        target?.draw(canvas)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        target?.alpha = alpha
    }

    override fun onBoundsChange(bounds: Rect) {
        target?.bounds = bounds
    }

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        target?.colorFilter = colorFilter
    }

}
