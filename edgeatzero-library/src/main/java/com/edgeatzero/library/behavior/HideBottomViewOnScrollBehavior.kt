package com.edgeatzero.library.behavior

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewPropertyAnimator
import androidx.annotation.Dimension
import androidx.annotation.IntDef
import androidx.annotation.RestrictTo
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.edgeatzero.library.R
import com.edgeatzero.library.common.TagHolder
import com.edgeatzero.library.common.WeakProperty
import com.google.android.material.animation.AnimationUtils

open class HideBottomViewOnScrollBehavior<V : View> @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<V>(context, attrs) {

    private var height = 0

    @State
    private var mState = STATE_SHOWED

    @State
    var state: Int
        get() = mState
        set(value) {
            if (mState == value) return
            val child = child ?: return
            if (value == STATE_SHOWED) show(child) else hide(child)
        }

    private var additionalHiddenOffsetY = 0

    private var target = View.NO_ID

    @Mode
    var mode = MODE_UP
        set(value) {
            if (field == value) return
            field = value
        }

    private var initialed = false

    private var animator: ViewPropertyAnimator? = null

    private var child by WeakProperty<V>()

    init {
        if (context != null && attrs != null) init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet) {

        val attr = context.obtainStyledAttributes(attrs, R.styleable.HideBottomViewOnScrollBehavior)
        try {
            target =
                attr.getResourceId(R.styleable.HideBottomViewOnScrollBehavior_target, View.NO_ID)
            mode = attr.getInt(R.styleable.HideBottomViewOnScrollBehavior_mode, MODE_UP)
        } catch (e: RuntimeException) {
            e.printStackTrace()
        } finally {
            attr.recycle()
        }
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout, child: V, layoutDirection: Int
    ): Boolean {
        if (this.child == null) this.child = child
        val paramsCompat = child.layoutParams as MarginLayoutParams
        height = child.measuredHeight + paramsCompat.bottomMargin
        if (mode == MODE_DOWN && !initialed) {
            child.translationY = (height + additionalHiddenOffsetY).toFloat()
            mState = STATE_HIDDEN
            initialed = true
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    fun setAdditionalHiddenOffsetY(child: V, @Dimension offset: Int) {
        additionalHiddenOffsetY = offset
        if (mState == STATE_HIDDEN) {
            child.translationY = (height + additionalHiddenOffsetY).toFloat()
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return (this.target == View.NO_ID || this.target == target.id) && nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyConsumed > 0) {
            if (mode == MODE_UP) hide(child) else show(child)
        } else if (dyConsumed < 0) {
            if (mode == MODE_UP) show(child) else hide(child)
        }
    }

    private fun show(child: V) {
        if (mState == STATE_SHOWED) return
        animator?.let {
            it.cancel()
            child.clearAnimation()
        }
        animateChildTo(
            child,
            0,
            ENTER_ANIMATION_DURATION,
            AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
        )
        mState = STATE_SHOWED
    }

    private fun hide(child: V) {
        if (mState == STATE_HIDDEN) return
        animator?.let {
            it.cancel()
            child.clearAnimation()
        }
        mState = STATE_HIDDEN
        animateChildTo(
            child,
            height + additionalHiddenOffsetY,
            EXIT_ANIMATION_DURATION,
            AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR
        )
    }

    private fun animateChildTo(
        child: V,
        targetY: Int,
        duration: Long,
        interpolator: TimeInterpolator
    ) {
        animator = child
            .animate()
            .translationY(targetY.toFloat())
            .setInterpolator(interpolator)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    animator = null
                }
            })
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @IntDef(value = [STATE_SHOWED, STATE_HIDDEN])
    @Retention(AnnotationRetention.SOURCE)
    annotation class State

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @IntDef(value = [MODE_UP, MODE_DOWN])
    @Retention(AnnotationRetention.SOURCE)
    annotation class Mode

    companion object : TagHolder(HideBottomViewOnScrollBehavior::class) {

        protected const val ENTER_ANIMATION_DURATION = 225L
        protected const val EXIT_ANIMATION_DURATION = 175L

        const val STATE_HIDDEN = 1
        const val STATE_SHOWED = 2

        const val MODE_UP = 0
        const val MODE_DOWN = 1

        @JvmStatic
        fun <V : View> from(view: V): HideBottomViewOnScrollBehavior<V> {
            val params = view.layoutParams
            require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
            val behavior = params.behavior
            require(behavior is HideBottomViewOnScrollBehavior<*>) { "The view is not associated with BottomSheetBehavior" }
            @Suppress("UNCHECKED_CAST")
            return behavior as HideBottomViewOnScrollBehavior<V>
        }

    }

}
