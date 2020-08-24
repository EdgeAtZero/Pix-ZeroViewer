package com.edgeatzero.library.util

import android.content.Context
import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.annotation.Dimension
import com.edgeatzero.library.ext.isPortrait
import kotlin.math.roundToInt

object DisplayUtils {

    @Dimension(unit = Dimension.PX)
    fun dip2px(
        @Dimension(unit = Dimension.DP) dip: Float,
        context: Context = Utils.context
    ): Float = dip.times(context.resources.displayMetrics.density)

    @Dimension(unit = Dimension.PX)
    fun dip2px(
        @Dimension(unit = Dimension.DP) dip: Int,
        context: Context = Utils.context
    ): Int = dip2px(dip = dip.toFloat(), context = context).roundToInt()

    @Dimension(unit = Dimension.SP)
    fun dip2sp(
        @Dimension(unit = Dimension.DP) dip: Float,
        context: Context = Utils.context
    ): Float = px2sp(px = dip2px(dip = dip, context = context), context = context)

    @Dimension(unit = Dimension.SP)
    fun dip2sp(
        @Dimension(unit = Dimension.DP) dip: Int,
        context: Context = Utils.context
    ): Int = dip2sp(dip = dip.toFloat(), context = context).roundToInt()

    @Dimension(unit = Dimension.DP)
    fun px2dip(
        @Dimension(unit = Dimension.PX) px: Float,
        context: Context = Utils.context
    ): Float = px.div(context.resources.displayMetrics.density)

    @Dimension(unit = Dimension.DP)
    fun px2dip(
        @Dimension(unit = Dimension.PX) px: Int,
        context: Context = Utils.context
    ): Int = px2dip(px = px.toFloat(), context = context).roundToInt()

    @Dimension(unit = Dimension.SP)
    fun px2sp(
        @Dimension(unit = Dimension.PX) px: Float,
        context: Context = Utils.context
    ): Float = px.div(context.resources.displayMetrics.scaledDensity)

    @Dimension(unit = Dimension.SP)
    fun px2sp(
        @Dimension(unit = Dimension.PX) px: Int,
        context: Context = Utils.context
    ): Int = px2sp(px = px.toFloat(), context = context).roundToInt()

    @Dimension(unit = Dimension.PX)
    fun sp2px(
        @Dimension(unit = Dimension.SP) sp: Float,
        context: Context = Utils.context
    ): Float = sp.times(context.resources.displayMetrics.scaledDensity)

    @Dimension(unit = Dimension.PX)
    fun sp2px(
        @Dimension(unit = Dimension.SP) sp: Int,
        context: Context = Utils.context
    ): Int = sp2px(sp = sp.toFloat(), context = context).roundToInt()

    @Dimension(unit = Dimension.DP)
    fun sp2dip(
        @Dimension(unit = Dimension.SP) sp: Float,
        context: Context = Utils.context
    ): Float = px2sp(px = sp2px(sp = sp, context = context), context = context)

    @Dimension(unit = Dimension.DP)
    fun sp2dip(
        @Dimension(unit = Dimension.SP) sp: Int,
        context: Context = Utils.context
    ): Int = sp2dip(sp = sp.toFloat(), context = context).roundToInt()

    fun getWidth(
        context: Context = Utils.context
    ): Int = context.resources.displayMetrics.widthPixels

    fun getHeight(
        context: Context = Utils.context
    ): Int = context.resources.displayMetrics.heightPixels

    fun getStatusBarHeight(
        context: Context = Utils.context
    ): Float {
        val id = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (id > 0) context.resources.getDimension(id)
        else 0F
    }

    fun getNavigationBarHeight(
        context: Context = Utils.context
    ): Float {
        val id = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (id > 0) context.resources.getDimension(id)
        else 0F
    }

}
