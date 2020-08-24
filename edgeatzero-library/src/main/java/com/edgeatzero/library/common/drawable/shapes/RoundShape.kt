package com.edgeatzero.library.common.drawable.shapes

import android.graphics.*
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import java.util.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
open class RoundShape : PathShape {

    var radius: FloatArray
        get() = field
        set(@NonNull radius) {
            if (radius.size < 8) throw ArrayIndexOutOfBoundsException("radii must have >= 8 values")
            field = radius
            path.reset()
            path.addRoundRect(rect, radius, Path.Direction.CW)
        }

    override val path: Path = Path()

    private val rect: RectF = RectF()

    constructor() : this(FloatArray(8).apply { fill(15F, 0, 7) })

    constructor(@NonNull radius: FloatArray) {
        if (radius.size < 8) throw ArrayIndexOutOfBoundsException("radii must have >= 8 values")
        this.radius = radius
    }

    override fun hashCode(): Int = Objects.hash(super.hashCode(), radius)

    @Throws(CloneNotSupportedException::class)
    override fun clone(): RoundShape {
        val shape = super.clone() as RoundShape
        shape.radius = radius.clone()
        return shape
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        if (!super.equals(other)) {
            return false
        }
        val shape = other as RoundShape
        return radius.contentEquals(shape.radius)
    }

    override fun onResize(width: Float, height: Float) {
        rect.set(0F, 0F, width, height)
        path.reset()
        path.addRoundRect(rect, radius, Path.Direction.CW)
    }
}
