package com.edgeatzero.library.common.text.style

import com.edgeatzero.library.common.drawable.InvalidateDrawable

/**
 * Created by sunhapper on 2019/1/25 .
 */
interface RefreshSpan {

    fun getInvalidateDrawable(): InvalidateDrawable?

}
