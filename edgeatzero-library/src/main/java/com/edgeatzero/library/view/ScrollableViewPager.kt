package com.edgeatzero.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.edgeatzero.library.R

class ScrollableViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    var scrollable: Boolean

    init {
        val attr = context.obtainStyledAttributes(
            attrs,
            R.styleable.ScrollableViewPager,
            0,
            0
        )
        scrollable = attr.getBoolean(R.styleable.ScrollableViewPager_scrollable, true)
        attr.recycle()
    }

    override fun onInterceptTouchEvent(motionEvent: MotionEvent): Boolean =
        scrollable && super.onInterceptTouchEvent(motionEvent)

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean =
        scrollable && super.onTouchEvent(motionEvent)

}
