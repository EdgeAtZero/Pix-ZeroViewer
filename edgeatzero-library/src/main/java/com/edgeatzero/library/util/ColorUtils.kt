package com.edgeatzero.library.util

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt

object ColorUtils {

    fun alphaColor(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Int =
        Color.argb(
            (255 * ratio).toInt(),
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )

    fun isLight(@ColorInt color: Int): Boolean = ColorUtils.calculateLuminance(color) >= 0.5

    fun alphaAsInt(alpha: Float): Int {
        return 0.coerceAtLeast((alpha * 255f).roundToInt().coerceAtMost(0xff))
    }

}
