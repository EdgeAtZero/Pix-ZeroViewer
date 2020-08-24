package com.edgeatzero.library.common.drawable.shapes

import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.shapes.Shape

open class PathShape : Shape() {

    open val path: Path = Path()

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawPath(path, paint)
    }

    override fun getOutline(outline: Outline) {
        outline.setConvexPath(path)
    }

}
