package com.edgeatzero.library.ext

import android.content.Context
import com.edgeatzero.library.interfaces.TitleHolder
import com.edgeatzero.library.util.Utils

fun TitleHolder.title(
    context: Context = Utils.context
): CharSequence? = getTitleRes()?.let { context.getString(it) } ?: getTitleText()
