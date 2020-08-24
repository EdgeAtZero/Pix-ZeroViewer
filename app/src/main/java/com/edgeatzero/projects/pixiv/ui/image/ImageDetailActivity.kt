package com.edgeatzero.projects.pixiv.ui.image

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.observe
import androidx.viewpager.widget.ViewPager
import com.edgeatzero.library.base.BaseActivity
import com.edgeatzero.library.ext.bundleProducer
import com.edgeatzero.library.ext.invokeDelegate
import com.edgeatzero.library.ext.viewModels
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.databinding.ActivityImageDetailBinding
import com.edgeatzero.projects.pixiv.ui.common.extras_index
import com.edgeatzero.projects.pixiv.ui.common.extras_urls
import com.edgeatzero.projects.pixiv.util.Settings

class ImageDetailActivity : BaseActivity<ActivityImageDetailBinding>() {

    override val binding by binding(R.layout.activity_image_detail)

    private val urls by bundleProducer.invokeDelegate(
        key = extras_urls,
        getter = Bundle::getStringArrayList
    )
    private val index by bundleProducer.invokeDelegate(
        key = extras_index,
        getter = Bundle::getInt
    )

    private val model by viewModels<ImageDetailViewModel>()

    private val adapter by lazy {
        object :
            FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            var data: List<String>? = null
                set(value) {
                    field = value
                    notifyDataSetChanged()
                    binding.ViewPager.setCurrentItem(model.currentItem.value ?: 0, false)
                }

            override fun getItem(position: Int): Fragment {
                val fragment = ImageDetailFragment()
                fragment.url = data?.get(position)
                return fragment
            }

            override fun getCount(): Int {
                return data?.size ?: 0
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        delegate.localNightMode = Settings.nightMode
        super.onCreate(savedInstanceState)
        isLayoutFullscreen = true
        isFullscreen = true
        isHideNavigation = true
        isImmersiveSitcky = true
        statusBarColor = Color.TRANSPARENT

        binding.ViewPager.apply {
            adapter = this@ImageDetailActivity.adapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageScrollStateChanged(state: Int) = Unit

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) = Unit

                override fun onPageSelected(position: Int) {
                    model.postCurrentItem(position)
                }

            })
        }

        binding.model = model
        model.urls.observe(this) { adapter.data = it }

        urls?.let { model.postUrls(it) }
        model.currentItem.value ?: kotlin.run { model.postCurrentItem(index) }
    }

}
