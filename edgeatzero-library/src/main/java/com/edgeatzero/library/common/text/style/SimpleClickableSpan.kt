package com.edgeatzero.library.common.text.style

import android.text.TextPaint
import android.text.style.ClickableSpan

abstract class SimpleClickableSpan : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) = Unit

}
