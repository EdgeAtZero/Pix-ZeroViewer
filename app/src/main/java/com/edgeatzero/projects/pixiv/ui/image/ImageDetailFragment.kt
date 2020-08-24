package com.edgeatzero.projects.pixiv.ui.image

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.edgeatzero.library.base.BaseFragment
import com.edgeatzero.library.common.BundleDelegate
import com.edgeatzero.library.ext.bundleProducer
import com.edgeatzero.library.ext.invokeDelegate
import com.edgeatzero.library.ext.viewModels
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.library.util.HttpMessageConstant.message_unknown_error
import com.edgeatzero.projects.pixiv.databinding.FragmentImageDetailBinding

class ImageDetailFragment : BaseFragment<FragmentImageDetailBinding>() {

    override val binding by binding()

    var url by bundleProducer.invokeDelegate(
        getter = Bundle::getString,
        setter = Bundle::putString
    )

    private val model by viewModels<ImageDetailItemViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.PhotoView.setOnPhotoTapListener { _, _, _ -> activity?.onBackPressed() }
        binding.PhotoView.setOnOutsidePhotoTapListener { activity?.onBackPressed() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.url.observe(viewLifecycleOwner) { loadIllustration(it) }
        url?.let { model.postUrl(it) }
    }

    private fun loadIllustration(url: String) {
        binding.state = ResponseState.LOADING
        Glide.with(binding.root)
            .asBitmap()
            .load(url)
            .listener(object : RequestListener<Bitmap> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.retry = { loadIllustration(url) }
                    binding.state = ResponseState.failed(e?.message ?: message_unknown_error)
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource ?: return false
                    binding.state = ResponseState.SUCCESSFUL
                    return false
                }

            })
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(binding.PhotoView)
    }

}
