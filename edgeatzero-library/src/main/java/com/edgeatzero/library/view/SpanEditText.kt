package com.edgeatzero.library.view

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.text.Spannable
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.getSystemService
import androidx.core.text.toSpannable
import com.edgeatzero.library.common.text.EditableFactory
import com.edgeatzero.library.common.text.SpanChangedWatcher
import com.edgeatzero.library.common.text.style.IntegratedSpan
import com.edgeatzero.library.ext.*
import com.edgeatzero.library.interfaces.KeyEventProxy

@SuppressLint("AppCompatCustomView")
class SpanEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.editTextStyle,
    @StyleRes defStyleRes: Int = 0
) : EditText(context, attrs, defStyleAttr, defStyleRes) {

    var keyEventProxy: KeyEventProxy = KeyEventProxy.DefaultKeyEventProxy()

    private val onTextContextMenuItemListeners by lazy { ArrayList<OnTextContextMenuItemListener>() }

    private val onKeyPreImeListeners by lazy { ArrayList<OnKeyPreImeListener>() }

    init {
        setEditableFactory(EditableFactory(SpanChangedWatcher()))
        setOnKeyListener { _, _, event ->
            text?.let { keyEventProxy.onKeyEvent(event, it) } ?: false
        }
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        return FixInputConnection(super.onCreateInputConnection(outAttrs)) { event ->
            text?.let { keyEventProxy.onKeyEvent(event, it) } ?: false
        }
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        return onKeyPreImeListeners.any {
            it.onKeyPreIme(keyCode, event)
        } || super.onKeyPreIme(keyCode, event)
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        return onTextContextMenuItemListeners.any {
            it.onTextContextMenuItem(id)
        } || super.onTextContextMenuItem(id)
    }

    class FixInputConnection(
        target: InputConnection,
        mutable: Boolean = true,
        private val handler: ((KeyEvent) -> Boolean)
    ) : InputConnectionWrapper(target, mutable) {

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            return if (beforeLength == 1 && afterLength == 0) {
                val event0 = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)
                val event1 = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL)
                sendKeyEvent(event0) && sendKeyEvent(event1)
            } else super.deleteSurroundingText(beforeLength, afterLength)
        }

        override fun sendKeyEvent(event: KeyEvent): Boolean {
            return handler.invoke(event) || super.sendKeyEvent(event)
        }

    }

    fun addOnTextContextMenuItemListener(listener: OnTextContextMenuItemListener): Boolean {
        if (!onTextContextMenuItemListeners.contains(listener)) {
            return onTextContextMenuItemListeners.add(listener)
        }
        return false
    }

    fun removeOnTextContextMenuItemListener(listener: OnTextContextMenuItemListener): Boolean {
        return onTextContextMenuItemListeners.remove(listener)
    }

    fun hasOnTextContextMenuItemListeners(): Boolean {
        return onTextContextMenuItemListeners.isNotEmpty()
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

    interface OnTextContextMenuItemListener {

        fun onTextContextMenuItem(id: Int): Boolean

        class DefaultOnTextContextMenuItemListener(
            private val editText: SpanEditText,
            private val transform: (source: Spannable) -> Spannable
        ) : OnTextContextMenuItemListener {

            override fun onTextContextMenuItem(id: Int): Boolean {
                if (id == android.R.id.paste) {
                    val manager = editText.context.getSystemService<ClipboardManager>()
                    val primaryClip = manager?.primaryClip ?: return false
                    val text = editText.text ?: return false
                    val selectionRange = if (editText.isFocused) text.selectionRange()
                    else IntRange(0, text.length)
                    var first = true
                    primaryClip.forEach {
                        val source = transform(it.coerceToText(editText.context).toSpannable())
                        if (first) {
                            text.select(selectionRange.last)
                            text.get<IntegratedSpan>(selectionRange).forEach { span ->
                                text.removeSpan(span)
                            }
                            text.replace(selectionRange.first, selectionRange.last, source)
                            first = false
                        } else {
                            text.insert(text.selectionEnd(), "\\n")
                            text.insert(text.selectionEnd(), source)
                        }
                    }
                    return true
                }
                return false
            }

        }

    }

}
