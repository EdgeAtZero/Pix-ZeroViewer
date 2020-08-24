package com.edgeatzero.library.common

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition
import kotlin.math.roundToInt

class UniformScaleTransformation(
    view: ImageView,
    private val changeType: ChangeType = ChangeType.NONE
) : ImageViewTarget<Bitmap>(view) {

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        super.onResourceReady(resource, transition)
        val ratio = resource.width.toDouble().div(resource.height)
        when (changeType) {
            ChangeType.NONE -> Unit
            ChangeType.WIDTH -> view.updateLayoutParams {
                width = view.height.times(ratio).roundToInt()
            }
            ChangeType.HEIGHT -> view.updateLayoutParams {
                height = view.width.div(ratio).roundToInt()
            }
        }
    }

    override fun setResource(resource: Bitmap?) {
        resource ?: return
        view.setImageBitmap(resource)
    }

    enum class ChangeType() {
        NONE,
        WIDTH,
        HEIGHT
    }

}
