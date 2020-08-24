package com.edgeatzero.projects.pixiv.ui.common

interface ScrollableCallback {

    fun onScrollToTop(fast: Boolean)

    val isScrolled: Boolean

}
