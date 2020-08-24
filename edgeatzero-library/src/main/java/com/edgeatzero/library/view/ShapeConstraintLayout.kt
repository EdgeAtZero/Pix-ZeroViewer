package com.edgeatzero.library.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.edgeatzero.library.R
import com.edgeatzero.library.common.drawable.shapes.CircleShape
import com.edgeatzero.library.common.drawable.shapes.PathShape
import com.edgeatzero.library.common.drawable.shapes.RoundShape


class ShapeConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var radius: FloatArray = FloatArray(8)

    private var circle: Boolean

    private var shape: PathShape

    init {
        val attr = context.obtainStyledAttributes(
            attrs,
            R.styleable.ShapeConstraintLayout,
            defStyleAttr,
            0
        )

        attr.getDimension(R.styleable.ShapeConstraintLayout_radius, -1F).also {
            if (it == -1F) {
                radius.fill(
                    attr.getDimension(R.styleable.ShapeConstraintLayout_radiusLeftTop, 0F),
                    0,
                    2
                )
                radius.fill(
                    attr.getDimension(R.styleable.ShapeConstraintLayout_radiusRightTop, 0F),
                    2,
                    4
                )
                radius.fill(
                    attr.getDimension(R.styleable.ShapeConstraintLayout_radiusRightBottom, 0F),
                    4,
                    6
                )
                radius.fill(
                    attr.getDimension(R.styleable.ShapeConstraintLayout_radiusLeftBottom, 0F),
                    6,
                    8
                )
            } else {
                radius.fill(it, 0, 8)
            }
        }

        circle = attr.getBoolean(R.styleable.ShapeConstraintLayout_circle, false)

        shape = if (circle) CircleShape() else RoundShape(radius)

        attr.recycle()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.clipPath(shape.path)
        super.dispatchDraw(canvas)
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        canvas?.clipPath(shape.path)
        return super.drawChild(canvas, child, drawingTime)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.clipPath(shape.path)
        super.onDraw(canvas)
    }

    override fun onDrawForeground(canvas: Canvas?) {
        canvas?.clipPath(shape.path)
        super.onDrawForeground(canvas)
    }

    override fun draw(canvas: Canvas?) {
        canvas?.clipPath(shape.path)
        super.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shape.resize(w.toFloat(), h.toFloat())
        super.invalidate()
        super.invalidateOutline()
    }

    fun getCircle(): Boolean = this.circle

    fun setCircle(boolean: Boolean) {
        if (circle != boolean) {
            circle = boolean
            shape = if (circle) CircleShape() else RoundShape(radius)
        }
    }

    private fun setRadius(element: Float, fromIndex: Int, toIndex: Int) =
        setRadius(element, fromIndex, toIndex, false)

    private fun setRadius(element: Float, fromIndex: Int, toIndex: Int, needInvalidate: Boolean) {
        radius.fill(element, fromIndex, toIndex)
        if (needInvalidate) {
            (shape as RoundShape?)?.radius = radius
            invalidate()
            invalidateOutline()
        }
    }

    fun setRadius(radius: Float) = setRadius(radius, 0, 8, !circle)

    fun setRadius(radius: FloatArray) {
        for (i in radius.indices) {
            radius[i] = radius[i]
        }
        if (!circle) {
            (shape as RoundShape?)?.radius = radius
            invalidate()
            invalidateOutline()
        }
    }

    fun setRadius(leftTop: Float, rightTop: Float, rightBottom: Float, leftBottom: Float) {
        setRadius(leftTop, 0, 2)
        setRadius(rightTop, 2, 4)
        setRadius(rightBottom, 4, 6)
        setRadius(leftBottom, 6, 8, !circle)
    }

    fun setLeftTopRadius(radius: Float) = setRadius(radius, 0, 2, !circle)

    fun setRightTopRadius(radius: Float) = setRadius(radius, 2, 4, !circle)

    fun setRightBottomRadius(radius: Float) = setRadius(radius, 4, 6, !circle)

    fun setLeftBottomRadius(radius: Float) = setRadius(radius, 6, 8, !circle)

}
