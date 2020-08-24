//package com.edgeatzero.library.view
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewConfiguration
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import androidx.annotation.AttrRes
//import androidx.annotation.StyleRes
//import androidx.viewpager2.widget.ViewPager2
//import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
//import com.edgeatzero.library.R
//import com.edgeatzero.library.common.WeakProperty
//import kotlin.math.absoluteValue
//import kotlin.math.sign
//
//class NestedScrollableHost @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    @AttrRes defStyleAttr: Int = 0,
//    @StyleRes defStyleRes: Int = 0
//) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
//
//    private var touchSlop = 0
//    private var initialX = 0f
//    private var initialY = 0f
//    private var targetId = View.NO_ID
//    private val target : ViewGroup?
//        get() {
//            var v: View? = parent as? View
//            while (v != null && (v !is ViewGroup || (v.id != viewPagerId && viewPagerId != View.NO_ID))) {
//                v = v.parent as? View
//            }
//            return v as? ViewGroup
//        }
//    private var viewPagerId = View.NO_ID
//    private val viewPager: ViewPager2?
//        get() {
//            var v: View? = parent as? View
//            while (v != null && (v !is ViewPager2 || (v.id != viewPagerId && viewPagerId != View.NO_ID))) {
//                v = v.parent as? View
//            }
//            return v as? ViewPager2
//        }
//
//    private var child by WeakProperty<View>()
//
//    init {
//        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
//        val attr = context.obtainStyledAttributes(
//            attrs,
//            R.styleable.NestedScrollableHost,
//            defStyleAttr,
//            defStyleRes
//        )
//        try {
//            viewPagerId = attr.getResourceId(R.styleable.NestedScrollableHost_viewPagerId, NO_ID)
//            targetId = attr.getResourceId(R.styleable.NestedScrollableHost_targetId, View.NO_ID)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            attr.recycle()
//        }
//    }
//
//    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
//        check(childCount <= 0) { "NestedScrollableHost can host only one direct child" }
//        this.child = child
//        super.addView(child, index, params)
//    }
//
//    private fun View?.canScroll(orientation: Int, delta: Float): Boolean {
//        val direction = -delta.sign.toInt()
//        return when (orientation) {
//            0 -> this?.canScrollHorizontally(direction) ?: false
//            1 -> this?.canScrollVertically(direction) ?: false
//            else -> throw IllegalArgumentException()
//        }
//    }
//
////    private fun canChildScroll()
//
//    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
//        handleInterceptTouchEvent(e)
//        return super.onInterceptTouchEvent(e)
//    }
//
//    private fun handleInterceptTouchEvent(e: MotionEvent) {
////        val parent = parentViewPager ?: return
//        val orientation = viewPager?.orientation ?: return
//
//        // Early return if child can't scroll in same direction as parent
//        if (!child.canScroll(orientation, -1f) && !child.canScroll(orientation, 1f)) {
//            return
//        }
//
//        if (e.action == MotionEvent.ACTION_DOWN) {
//            initialX = e.x
//            initialY = e.y
//            parent.requestDisallowInterceptTouchEvent(true)
//        } else if (e.action == MotionEvent.ACTION_MOVE) {
//            val dx = e.x - initialX
//            val dy = e.y - initialY
//            val isVpHorizontal = orientation == ORIENTATION_HORIZONTAL
//
//            // assuming ViewPager2 touch-slop is 2x touch-slop of child
//            val scaledDx = dx.absoluteValue * if (isVpHorizontal) .5f else 1f
//            val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else .5f
//
//            if (scaledDx > touchSlop || scaledDy > touchSlop) {
//                if (isVpHorizontal == (scaledDy > scaledDx)) {
//                    // Gesture is perpendicular, allow all parents to intercept
//                    parent.requestDisallowInterceptTouchEvent(false)
//                } else {
//                    // Gesture is parallel, query child if movement in that direction is possible
//                    if (child.canScroll(orientation, if (isVpHorizontal) dx else dy)) {
//                        // Child can scroll, disallow all parents to intercept
//                        parent.requestDisallowInterceptTouchEvent(true)
//                    } else {
//                        // Child cannot scroll, allow all parents to intercept
//                        parent.requestDisallowInterceptTouchEvent(false)
//                    }
//                }
//            }
//        }
//    }
//}
