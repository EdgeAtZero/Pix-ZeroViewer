package com.edgeatzero.library.interfaces

import android.text.Editable
import android.text.style.CharacterStyle
import android.view.KeyEvent
import com.edgeatzero.library.common.text.style.DeleteDelegateSpan
import com.edgeatzero.library.common.text.style.IntegratedSpan
import com.edgeatzero.library.ext.*

interface KeyEventProxy {

    fun onKeyEvent(event: KeyEvent, source: Editable): Boolean

    class DpadKeyEvent : KeyEventProxy {
        override fun onKeyEvent(event: KeyEvent, source: Editable): Boolean {
            if (event.action != KeyEvent.ACTION_DOWN) return false
            val selection = source.selectionRange()
            source.get<IntegratedSpan>(selection).forEach {
                val span = source.spanRange(it)
                if (span.contains(selection)) {
                    val index = when (event.keyCode) {
                        //光标左移事件
                        KeyEvent.KEYCODE_DPAD_LEFT -> span.first.minus(1)
                        //光标右移事件
                        KeyEvent.KEYCODE_DPAD_RIGHT -> span.last.plus(1)
                        else -> return false
                    }
                    source.select(index, true)
                    return true
                }
            }
            return false
        }

    }

    class DefaultKeyEventProxy : KeyEventProxy {

        private val dpadKeyEvent = DpadKeyEvent()

        override fun onKeyEvent(event: KeyEvent, source: Editable): Boolean {
            if (dpadKeyEvent.onKeyEvent(event, source)) return true
            if (event.keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                val selection = source.selectionRange()
                source.get<CharacterStyle>(selection).forEach {
                    val spanRange = source.spanRange(it)
                    if (it is DeleteDelegateSpan) {
                        val length = spanRange.last - spanRange.first
                        val start = selection.first .minus(spanRange.first).coerceAtLeast(0)
                        val end = selection.last.minus(spanRange.last).coerceAtMost(length)
                        if (it.deletable(source.substring(spanRange), start, end)) {
                            if (it is IntegratedSpan) {
                                source.removeSpan(it)
                                source.delete(spanRange)
                                return true
                            }
                        }
                    }
                    if (it is IntegratedSpan) {
                        source.removeSpan(it)
                        source.delete(spanRange)
                        return true
                    }
                }
            }
            return false
        }

    }

}
