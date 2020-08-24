@file:SuppressLint("AppCompatCustomView")

package com.edgeatzero.library.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes

class AutoVisibilityTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : TextView(context, attrs, defStyleAttr, defStyleRes) {

    override fun setAlpha(alpha: Float) {
        super.setAlpha(alpha)
        visibility = if (text.isEmpty() || alpha == 0f) View.GONE else View.VISIBLE
    }

}
