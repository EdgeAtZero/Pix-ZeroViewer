package com.edgeatzero.library.common.text

import android.text.SpanWatcher
import android.text.Spannable
import com.edgeatzero.library.common.text.style.BreakableSpan
import com.edgeatzero.library.common.text.style.DeleteDelegateSpan
import com.edgeatzero.library.common.text.style.IntegratedSpan
import com.edgeatzero.library.ext.contains
import com.edgeatzero.library.ext.select
import com.edgeatzero.library.ext.selectionRange
import com.edgeatzero.library.ext.spanRange

class SpanChangedWatcher : SpanWatcher {

    override fun onSpanChanged(
        text: Spannable?,
        what: Any?,
        ostart: Int,
        oend: Int,
        nstart: Int,
        nend: Int
    ) {
        text ?: return
        if (what is DeleteDelegateSpan) what.changed()
        if (what is BreakableSpan && what.canBreak(text)) text.removeSpan(what)
    }

    override fun onSpanRemoved(text: Spannable?, what: Any?, start: Int, end: Int) {
    }

    override fun onSpanAdded(text: Spannable?, what: Any?, start: Int, end: Int) {
        text ?: return
        val selectionRange = text.selectionRange()
        if (what is IntegratedSpan) {
            val spanRange = text.spanRange(what)
            if (selectionRange.contains(spanRange)) text.select(spanRange.last)
        }
    }

}
