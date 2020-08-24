package com.edgeatzero.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.view.*
import com.edgeatzero.library.R
import com.edgeatzero.library.common.TagHolder
import com.edgeatzero.library.common.WeakProperty
import com.edgeatzero.library.ext.logd
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.library.util.DisplayUtils
import kotlin.math.roundToInt

class StatusBarTintedLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = R.style.Widget_StatusBarTintedLayout
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object : TagHolder(StatusBarTintedLayout::class)

    var statusBarHeight: Int

    var enableStatusBarMargin: Boolean = false
        set(value) {
            field = value
            content?.safeUpdateLayoutParams<LayoutParams> {
                setMargins(marginLeft, marginTop + statusBarHeight, marginRight, marginBottom)
            }
        }

    private val statusBarBackground: View

    private var content by WeakProperty<View>()

    private var contentFitsSystemWindowsBackup: Boolean = true

    init {
        fitsSystemWindows = false
        statusBarHeight = DisplayUtils.getStatusBarHeight().roundToInt()
        statusBarBackground = View(context).apply {
            id = android.R.id.statusBarBackground
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, statusBarHeight, Gravity.TOP)
        }
        val attr = context.obtainStyledAttributes(
            attrs,
            R.styleable.StatusBarTintedLayout,
            defStyleAttr,
            defStyleRes
        )
        try {
            enableStatusBarMargin = attr.getBoolean(
                R.styleable.StatusBarTintedLayout_enableStatusBarMargin,
                enableStatusBarMargin
            )
            statusBarBackground.setBackgroundColor(attr.getColorOrThrow(R.styleable.StatusBarTintedLayout_statusBarBackgroundColor))
        } catch (e: Exception) {
            try {
                statusBarBackground.background =
                    attr.getDrawableOrThrow(R.styleable.StatusBarTintedLayout_statusBarBackgroundColor)
            } catch (E: Exception) {
            }
        } finally {
            attr.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (parent as ViewGroup).fitsSystemWindows = false
    }


    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child?.id == android.R.id.statusBarBackground) {
            super.addView(child, index, params)
            return
        }
        check(childCount <= 0) { "StatusBarTintedLayout can host only one direct child" }
        super.addView(child, index, params)
        this.content = child ?: return
        contentFitsSystemWindowsBackup = child.fitsSystemWindows
        child.fitsSystemWindows = true
        if (enableStatusBarMargin) child.updateLayoutParams<LayoutParams> {
            setMargins(marginLeft, marginTop + statusBarHeight, marginRight, marginBottom)
        }
        addView(statusBarBackground)
    }

    override fun onViewRemoved(child: View?) {
        if (child?.id == android.R.id.statusBarBackground) return
        super.onViewRemoved(child)
        child ?: return
        if (child != content) {
            logd("Unknown view(${child}) was removed from $this")
        } else {
            child.fitsSystemWindows = contentFitsSystemWindowsBackup
            removeView(statusBarBackground)
        }
    }

    protected fun setStatusBarBackgroundColor(color: Int) {
        statusBarBackground.setBackgroundColor(color)
    }

}
