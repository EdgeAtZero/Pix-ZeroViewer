package com.edgeatzero.library.common.text

import android.text.Editable
import android.text.NoCopySpan
import android.text.Spanned
import com.edgeatzero.library.common.WeakProperty

class EditableFactory constructor() : Editable.Factory() {

    companion object {

        const val DEFAULT_FLAGS = Spanned.SPAN_INCLUSIVE_INCLUSIVE or Spanned.SPAN_PRIORITY

    }

    constructor(vararg spans: NoCopySpan) : this() {
        this.spans.addAll(spans)
    }

    private var editable by WeakProperty<Editable>()

    private val spans = ArrayList<NoCopySpan>()

    override fun newEditable(source: CharSequence?): Editable {
        val editable = super.newEditable(source)
        this.editable = editable
        spans.forEach { editable.setSpan(it, 0, editable.length, DEFAULT_FLAGS) }
        return editable
    }

    fun addSpan(span: NoCopySpan): Boolean {
        return if (spans.indexOf(span) == -1) {
            val editable = editable
            editable?.setSpan(editable, 0, editable.length, DEFAULT_FLAGS)
            spans.add(span)
        } else false
    }

    fun removeSpan(span: NoCopySpan): Boolean {
        return if (spans.indexOf(span) != -1) {
            editable?.removeSpan(span)
            spans.remove(span)
        } else false
    }

    fun removeAllSpan(): Boolean {
        return spans.removeAll {
            editable?.removeSpan(it)
            true
        }
    }

}
