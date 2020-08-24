package com.edgeatzero.library.common.drawable.shapes

import android.graphics.*

class CircleShape : PathShape() {

    override val path: Path = Path()

    @Throws(CloneNotSupportedException::class)
    override fun clone(): CircleShape = super.clone() as CircleShape

    override fun equals(other: Any?): Boolean {
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        if (!super.equals(other)) {
            return false
        }
        return this === other
    }

    override fun onResize(width: Float, height: Float) {
        path.reset()
        path.addCircle(width / 2, height / 2, width.coerceAtMost(height) / 2, Path.Direction.CW)
    }
}
