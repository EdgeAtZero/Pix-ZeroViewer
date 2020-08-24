package com.edgeatzero.library.ext

import android.text.Selection
import android.text.Spannable
import android.text.Spanned
import android.view.inputmethod.BaseInputConnection as BIC

operator fun Spannable.set(start: Int = 0, end: Int = length, span: Any) {
    setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}

operator fun Spannable.set(range: IntRange, span: Any) {
    setSpan(span, range.first, range.last, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}

inline operator fun <reified T> Spannable.get(start: Int = 0, end: Int = length): Array<out T> {
    return getSpans(start, end, T::class.java) ?: emptyArray()
}

inline operator fun <reified T> Spannable.get(range: IntRange): Array<out T> {
    return getSpans(range.first, range.last, T::class.java) ?: emptyArray()
}

fun Spannable.spanRange(what: Any): IntRange {
    return IntRange(getSpanStart(what), getSpanEnd(what))
}

fun Spannable.selectionStart(): Int {
    return Selection.getSelectionStart(this)
}

fun Spannable.selectionEnd(): Int {
    return Selection.getSelectionEnd(this)
}

fun Spannable.selectionRange(): IntRange {
    return IntRange(Selection.getSelectionStart(this), Selection.getSelectionEnd(this))
}

fun Spannable.composingRange(): IntRange {
    return IntRange(BIC.getComposingSpanStart(this), BIC.getComposingSpanEnd(this))
}

@JvmOverloads
fun Spannable.select(index: Int, check: Boolean = false) {
    val value = if (check) index.coerceAtMost(length - 1).coerceAtLeast(0) else index
    Selection.setSelection(this, value)
}

@JvmOverloads
fun Spannable.select(range: IntRange, check: Boolean = false) {
    val start = if (check) range.first.coerceAtLeast(0) else range.first
    val end = if (check) range.last.coerceAtMost(length - 1).coerceAtLeast(0) else range.last
    Selection.setSelection(this, start, end)
}
