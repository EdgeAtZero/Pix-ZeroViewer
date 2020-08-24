@file:Suppress("UNUSED")

package com.edgeatzero.library.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.edgeatzero.library.R
import kotlin.math.roundToInt

class ExpandableLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object {

        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL

        const val KEY_MODE = "mode"
        const val KEY_SUPER = "super"
        const val KEY_EXPANSION = "expansion"

    }

    var state: State = State.COLLAPSED
        private set

    var duration: Long

    var interpolator: Interpolator = FastOutSlowInInterpolator()

    private var animator: ValueAnimator? = null

    private val onExpansionUpdateListeners by lazy { ArrayList<OnExpansionUpdateListener>() }

    private val onToggleListeners by lazy { ArrayList<OnToggleListener>() }

    var mode: Mode

    @LinearLayoutCompat.OrientationMode
    val orientation: Int
        get() = mode.orientation

    @FloatRange(from = 0.0, to = 1.0)
    private var mExpansion: Float

    var expansion: Float
        @FloatRange(from = 0.0, to = 1.0)
        get() = mExpansion
        set(@FloatRange(from = 0.0, to = 1.0) value) {
            if (mExpansion != value) {
                val delta = value - mExpansion
                val state = when {
                    value == 0F -> State.COLLAPSED
                    value == 1F -> State.EXPANDED
                    delta < 0F -> State.COLLAPSING
                    delta > 0F -> State.EXPANDING
                    else -> throw IllegalArgumentException()
                }
                visibility = if (state == State.COLLAPSED) GONE else VISIBLE
                mExpansion = value
                val old = this.state
                this.state = state
                requestLayout()
                onExpansionUpdateListeners.forEach { it.onExpansionUpdate(value, old, state) }
                if (state == State.COLLAPSED) onToggleListeners.forEach { it.onToggle(false) }
                else if (state == State.EXPANDED) onToggleListeners.forEach { it.onToggle(true) }
            }
        }

    val isExpanded
        get() = state == State.EXPANDED || state == State.EXPANDING

    val size
        get() = 21

    init {
        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.ExpandableLayout,
            defStyleAttr,
            defStyleRes
        )
        duration = array.getInt(R.styleable.ExpandableLayout_animationDuration, 200).toLong()
        mExpansion = if (array.getBoolean(R.styleable.ExpandableLayout_expanded, true)) 1F else 0F
        mode = Mode.values()[array.getInt(R.styleable.ExpandableLayout_expandMode, 3)]
        array.recycle()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        animator?.cancel()
        forEach {
            it.layoutParams
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        val height = measuredHeight

        val size = if (orientation == VERTICAL) height else width

//        visibility = if (!isExpanded && size == 0) GONE else VISIBLE
//
//        val expansionDelta = size.minus(size.times(expansion).roundToInt())
//        if (mParallax > 0) {
//            val parallaxDelta = expansionDelta.times(mParallax)
//            forEach { child ->
//                when (mode) {
//                    Mode.LEFT -> child.translationX = parallaxDelta
//                    Mode.TOP -> child.translationY = parallaxDelta
//                    Mode.RIGHT -> child.translationX = -parallaxDelta
//                    Mode.BOTTOM -> child.translationY = -parallaxDelta
//                }
//            }
//        }
//        if (orientation == VERTICAL) {
//            setMeasuredDimension(width, height.minus(expansionDelta))
//        } else {
//            setMeasuredDimension(width.minus(expansionDelta), height)
//        }

        visibility = if (!isExpanded && size == 0) GONE else VISIBLE

        val delta = size.times(mExpansion)

        val target = size.minus(delta)

        forEach { child ->
            when (mode) {
                Mode.LEFT -> child.translationX = target
                Mode.TOP -> child.translationY = target
                Mode.RIGHT -> child.translationY = -delta
                Mode.BOTTOM -> child.translationY = -target
            }
        }

        if (orientation == VERTICAL) {
            setMeasuredDimension(width, delta.roundToInt())
        } else {
            setMeasuredDimension(delta.roundToInt(), height)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as? Bundle ?: return
        mode = Mode[bundle.getInt(KEY_MODE)]
        expansion = bundle.getFloat(KEY_EXPANSION)
        super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPER))
    }

    override fun onSaveInstanceState(): Parcelable? {
        return bundleOf(
            KEY_SUPER to super.onSaveInstanceState(),
            KEY_EXPANSION to mExpansion,
            KEY_MODE to mode.value
        )
    }

    @JvmOverloads
    fun toggle(animate: Boolean = true) {
        if (isExpanded) collapse(animate) else expand(animate)
    }

    @JvmOverloads
    fun expand(animate: Boolean = true) {
        setExpanded(true, animate)
    }

    @JvmOverloads
    fun collapse(animate: Boolean = true) {
        setExpanded(false, animate)
    }

    @JvmOverloads
    fun setExpanded(expand: Boolean, animate: Boolean = true): Boolean {
        if (expand == isExpanded) return false
        val target = if (expand) 1F else 0F
        if (animate) animateSize(target) else expansion = target
        return true
    }

    private fun animateSize(targetExpansion: Float) {
        animator?.cancel()
        animator = null

        val animator = ValueAnimator.ofFloat(mExpansion, targetExpansion)
        animator.interpolator = interpolator
        animator.duration = duration
        animator.addUpdateListener { expansion = it.animatedValue as Float }
        animator.addListener(ExpansionListener(targetExpansion))
        this.animator = animator
        animator.start()
    }

    private inner class ExpansionListener(
        private val targetExpansion: Float
    ) : Animator.AnimatorListener {

        private var canceled = false

        override fun onAnimationStart(animation: Animator) {}

        override fun onAnimationEnd(animation: Animator) {
            if (!canceled) {
                expansion = targetExpansion
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            canceled = true
        }

        override fun onAnimationRepeat(animation: Animator) {}

    }

    fun addOnExpansionUpdateListener(listener: OnExpansionUpdateListener): Boolean {
        if (!onExpansionUpdateListeners.contains(listener)) {
            return onExpansionUpdateListeners.add(listener)
        }
        return false
    }

    fun removeOnExpansionUpdateListener(listener: OnExpansionUpdateListener): Boolean {
        return onExpansionUpdateListeners.remove(listener)
    }

    fun hasOnExpansionUpdateListener(): Boolean {
        return onExpansionUpdateListeners.isNotEmpty()
    }

    fun addOnToggleListener(listener: OnToggleListener): Boolean {
        if (!onToggleListeners.contains(listener)) {
            return onToggleListeners.add(listener)
        }
        return false
    }

    fun removeOnToggleListener(listener: OnToggleListener): Boolean {
        return onToggleListeners.remove(listener)
    }

    fun hasOnToggleListener(): Boolean {
        return onToggleListeners.isNotEmpty()
    }

    interface OnExpansionUpdateListener {

        fun onExpansionUpdate(expansion: Float, oldState: State, newState: State)

    }

    interface OnToggleListener {

        fun onToggle(expanded: Boolean)

    }

    enum class State { COLLAPSED, COLLAPSING, EXPANDING, EXPANDED; }

    enum class Mode(
        @IntRange(from = 0, to = 3) val value: Int,
        @LinearLayoutCompat.OrientationMode val orientation: Int
    ) {

        LEFT(0, HORIZONTAL), TOP(1, VERTICAL), RIGHT(2, HORIZONTAL), BOTTOM(3, VERTICAL);

        companion object {

            @JvmStatic
            operator fun get(value: Int): Mode {
                return values().first { it.value == value }
            }

        }

    }

}
