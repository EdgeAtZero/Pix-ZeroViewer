package com.edgeatzero.library.common.drawable

import com.edgeatzero.library.interfaces.RefreshListener

/**
 * Created by sunhapper on 2019/1/25 .
 */
interface InvalidateDrawable {
    var mRefreshListeners: MutableCollection<RefreshListener>
    fun addRefreshListener(callback: RefreshListener)
    fun removeRefreshListener(callback: RefreshListener)
    fun refresh()
}
