package com.edgeatzero.projects.pixiv.ui.common

import android.animation.Animator
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.edgeatzero.library.common.WeakProperty
import com.edgeatzero.library.view.SpanEditText
import com.edgeatzero.projects.pixiv.R
import androidx.appcompat.R as AxR

class SpanEditTextLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.editTextStyle,
    @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var editText by WeakProperty<SpanEditText>()

    private val clearButton by lazy {
        val button = AppCompatImageButton(context, null, AxR.attr.toolbarNavigationButtonStyle)
        button.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_clear))
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.END or Gravity.CENTER_VERTICAL
        button.layoutParams = params
        button.isVisible = false
        button
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child !is SpanEditText) {
            super.addView(child, index, params)
            return
        }
        check(childCount <= 0) { "StatusBarTintedLayout can host only one direct child" }
        super.addView(child, index, params)
        super.addView(clearButton)
        editText = child
        child.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                toggle(s?.length ?: 0 > 0)
            }

            private var animate: ViewPropertyAnimator? = null

            private var cache = false

            private fun toggle(display: Boolean) {
                if (cache == display) return
                animate?.cancel()
                val animate = clearButton.animate()
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .alpha(if (display) 1F else 0F)
                    .setListener(object : Animator.AnimatorListener {

                        override fun onAnimationStart(animation: Animator?) {
                            if (display) clearButton.isVisible = true
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            if (!display) clearButton.isVisible = false
                            animate = null
                            cache = display
                        }

                        override fun onAnimationCancel(animation: Animator?) {}

                        override fun onAnimationRepeat(animation: Animator?) {}

                    })
                this.animate = animate
                animate.start()
            }

        })
        clearButton.setOnClickListener { child.setText("") }
    }

}
