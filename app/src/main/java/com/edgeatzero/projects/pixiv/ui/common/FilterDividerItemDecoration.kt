package com.edgeatzero.projects.pixiv.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.RestrictTo
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import com.edgeatzero.library.ext.logd
import com.edgeatzero.projects.pixiv.R
import kotlin.math.roundToInt

open class FilterDividerItemDecoration constructor(
    private val context: Context,
    @Orientation orientation: Int
) : RecyclerView.ItemDecoration() {

    companion object {

        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL

        @JvmStatic
        private val ATTRS = intArrayOf(R.attr.filter_divider)

    }

    @Orientation
    private var mOrientation: Int = orientation
    var orientation
        @Orientation
        get() = mOrientation
        set(@Orientation value) {
            mOrientation = value
        }

    private val mBounds = Rect()

    private var mDrawable: Drawable?
    var drawable: Drawable?
        get() = mDrawable
        set(value) {
            mDrawable = value ?: throw IllegalArgumentException("Drawable cannot be null.")
        }

    fun drawable(@DrawableRes res: Int) {
        mDrawable = ContextCompat.getDrawable(context, res)
    }

    var filter: View.(Int) -> Boolean = { true }

    init {
        val attr = context.obtainStyledAttributes(ATTRS)
        mDrawable = attr.getDrawable(0)
        attr.recycle()
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        when (mOrientation) {
            VERTICAL -> drawVertical(canvas, parent)
            HORIZONTAL -> drawHorizontal(canvas, parent)
        }
    }

    protected open fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val mDrawable = mDrawable ?: return
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            val minus = parent.height.minus(parent.paddingBottom)
            canvas.clipRect(left, parent.paddingTop, right, minus)
        } else {
            left = 0
            right = parent.width
        }
        val filter = filter
        parent.forEach {
            if (!filter.invoke(it, parent.getChildAdapterPosition(it))) return@forEach
            parent.getDecoratedBoundsWithMargins(it, mBounds)
            val bottom: Int = mBounds.bottom + it.translationY.roundToInt()
            val top: Int = bottom - mDrawable.intrinsicHeight
            mDrawable.setBounds(left, top, right, bottom)
            mDrawable.draw(canvas)
        }
        canvas.restore()
    }

    protected open fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val layoutManager = parent.layoutManager ?: return
        val mDrawable = mDrawable ?: return
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height.minus(parent.paddingBottom)
            val minus = parent.width.minus(parent.paddingRight)
            canvas.clipRect(parent.paddingLeft, top, minus, bottom)
        } else {
            top = 0
            bottom = parent.height
        }
        val filter = filter
        parent.forEach {
            if (!filter.invoke(it, parent.getChildAdapterPosition(it))) return@forEach
            layoutManager.getDecoratedBoundsWithMargins(it, mBounds)
            val right: Int = mBounds.right + it.translationX.roundToInt()
            val left: Int = right - mDrawable.intrinsicWidth
            mDrawable.setBounds(left, top, right, bottom)
            mDrawable.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val mDrawable = mDrawable
        val index = parent.getChildAdapterPosition(view)
        if (!filter.invoke(view, index) || mDrawable == null) kotlin.run {
            outRect.set(0, 0, 0, 0)
            return
        }
        when (mOrientation) {
            VERTICAL -> outRect.set(0, 0, 0, mDrawable.intrinsicHeight)
            HORIZONTAL -> outRect.set(0, 0, mDrawable.intrinsicHeight, 0)
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    @IntDef(RecyclerView.HORIZONTAL, RecyclerView.VERTICAL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Orientation

}
