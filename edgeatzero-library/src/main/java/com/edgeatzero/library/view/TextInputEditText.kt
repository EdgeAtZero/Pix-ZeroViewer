package com.edgeatzero.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.annotation.AttrRes
import com.google.android.material.textfield.TextInputEditText

class TextInputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = com.google.android.material.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    private val onKeyPreImeListeners by lazy { ArrayList<OnKeyPreImeListener>() }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        return onKeyPreImeListeners.any {
            it.onKeyPreIme(keyCode, event)
        } || super.onKeyPreIme(keyCode, event)
    }

    fun addOnKeyPreImeListener(listener: OnKeyPreImeListener): Boolean {
        if (!onKeyPreImeListeners.contains(listener)) {
            return onKeyPreImeListeners.add(listener)
        }
        return false
    }

    fun removeOnKeyPreImeListener(listener: OnKeyPreImeListener): Boolean {
        return onKeyPreImeListeners.remove(listener)
    }

    fun hasOnKeyPreImeListener(): Boolean {
        return onKeyPreImeListeners.isNotEmpty()
    }

    interface OnKeyPreImeListener {

        fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean

    }


}
