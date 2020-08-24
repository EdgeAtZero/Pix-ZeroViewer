package com.edgeatzero.library.common.text.style

/**
 * 代理Span的删除
 */
interface DeleteDelegateSpan {

    fun deletable(source: String, start: Int, end: Int): Boolean

    fun changed()

}
