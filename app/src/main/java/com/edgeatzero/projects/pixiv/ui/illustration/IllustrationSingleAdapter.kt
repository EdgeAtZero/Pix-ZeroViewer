package com.edgeatzero.projects.pixiv.ui.illustration

import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.edgeatzero.library.base.BaseAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.common.UniformScaleTransformation
import com.edgeatzero.library.ext.bind
import com.edgeatzero.library.ext.startActivity
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.library.util.DisplayUtils
import com.edgeatzero.library.util.HttpMessageConstant.message_unknown_error
import com.edgeatzero.projects.pixiv.databinding.LayoutIllustrationSingleBinding
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.ui.common.extras_index
import com.edgeatzero.projects.pixiv.ui.common.extras_urls
import com.edgeatzero.projects.pixiv.ui.image.ImageDetailActivity

class IllustrationSingleAdapter @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null
) : BaseAdapter<DataBindingViewHolder<LayoutIllustrationSingleBinding>>(activity, fragment) {

    var data: Illustration? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun getItem(position: Int): String? {
        return data?.metaPage?.get(position)?.imageUrls?.large
    }

    override fun getItemCount() = data?.pageCount ?: 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = inflateHolder(parent)

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<LayoutIllustrationSingleBinding>,
        position: Int
    ) {
        val data = data ?: return
        val item = getItem(position) ?: return
        holder.bind { binding ->
            binding.ImageView.setOnClickListener {
                context().startActivity<ImageDetailActivity> {
                    bundleOf(
                        extras_urls to data.metaPage.map { it.imageUrls.original },
                        extras_index to position
                    )
                }
            }
            val w = DisplayUtils.getWidth()
            val h = DisplayUtils.getHeight() - DisplayUtils.dip2px(dip = 67)
            val illustrationH = data.height.toFloat() / data.width * w
            binding.ImageView.updateLayoutParams {
                this.height = illustrationH.toInt()
            }
            if (position == 0 && itemCount == 1) {
                if (illustrationH < h) {
                    binding.ImageView.updateLayoutParams<ViewGroup.LayoutParams> {
                        height = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                    binding.root.updateLayoutParams<ViewGroup.LayoutParams> {
                        height = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                    binding.ImageView.scaleType = ImageView.ScaleType.FIT_CENTER
                    loadIllustration(binding, item, false)
                } else {
                    binding.ImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    loadIllustration(binding, item, true)
                }
            } else {
                binding.ImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                loadIllustration(binding, item, true)
            }
        }
    }

    private fun loadIllustration(
        binding: LayoutIllustrationSingleBinding,
        url: String,
        changeSize: Boolean
    ) {
        binding.state = ResponseState.LOADING
        val type = if (changeSize) UniformScaleTransformation.ChangeType.HEIGHT
        else UniformScaleTransformation.ChangeType.NONE
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
                    binding.retry = { loadIllustration(binding, url, changeSize) }
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
            .into(UniformScaleTransformation(binding.ImageView, type))
    }

}
