@file:Suppress("DEPRECATION")

package com.edgeatzero.library.util

import android.graphics.drawable.ShapeDrawable
import com.edgeatzero.library.common.drawable.shapes.CircleShape

object DrawableUtil {

    fun generateCircleDrawable(): ShapeDrawable = ShapeDrawable(CircleShape())

}
