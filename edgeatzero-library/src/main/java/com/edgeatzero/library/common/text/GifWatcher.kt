package com.edgeatzero.library.common.text

import android.app.Activity
import android.text.SpanWatcher
import android.text.Spannable
import android.view.View
import com.edgeatzero.library.common.WeakProperty
import com.edgeatzero.library.common.text.style.RefreshSpan
import com.edgeatzero.library.ext.clear
import com.edgeatzero.library.interfaces.RefreshListener

class GifWatcher(view: View) : SpanWatcher, RefreshListener {

    private var mLastTime: Long = 0
    private val view by WeakProperty { view }

    override fun onSpanAdded(text: Spannable, what: Any, start: Int, end: Int) {
        if (what is RefreshSpan) what.getInvalidateDrawable()?.addRefreshListener(this)
    }

    override fun onSpanRemoved(text: Spannable, what: Any, start: Int, end: Int) {
        if (what is RefreshSpan) what.getInvalidateDrawable()?.removeRefreshListener(this)
    }

    override fun onSpanChanged(
        text: Spannable,
        what: Any,
        ostart: Int,
        oend: Int,
        nstart: Int,
        nend: Int
    ) {

    }

    override fun onRefresh(): Boolean {
        val view = view ?: return false
        val context = view.context
        if (context is Activity && (context.isDestroyed || context.isFinishing)) {
                clear()
                return false
        }
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastTime > REFRESH_INTERVAL) {
            mLastTime = currentTime
            view.invalidate()
        }
        return true
    }

    private fun clear() {
        ::view.clear()
    }

    companion object {
        private const val REFRESH_INTERVAL = 60
    }
}
