package com.edgeatzero.projects.pixiv.ui.common

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.adapters.AnimationAdapter

@Suppress("UNCHECKED_CAST")
class AnimatorAdapter @JvmOverloads constructor(
    adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
    var from: Float = DEFAULT_SCALE_FROM
) : AnimationAdapter(adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>) {

    companion object {

        const val DEFAULT_SCALE_FROM = .5f

    }

    init {
        setInterpolator(FastOutSlowInInterpolator())
    }

    override fun getAnimators(view: View?): Array<Animator> {
        view ?: return emptyArray()
        val fromFloat = view.measuredHeight.toFloat()
//        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, from, 1f)
//        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, from, 1f)
        val translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, fromFloat, 0f)
        return arrayOf(/*scaleX, scaleY, */translationY)
    }

}
