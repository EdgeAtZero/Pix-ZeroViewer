package com.edgeatzero.projects.pixiv.ui.illustration

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.edgeatzero.library.base.BaseActivity
import com.edgeatzero.library.common.BaseFragmentStatePagerAdapter
import com.edgeatzero.library.ext.bundleProducer
import com.edgeatzero.library.ext.invokeDelegate
import com.edgeatzero.library.ext.viewModels
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.databinding.ActivityIllustrationDetailBinding
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.ui.common.DataChannel
import com.edgeatzero.projects.pixiv.ui.common.OnBackPressedListenerFragment
import com.edgeatzero.projects.pixiv.ui.common.extras_index
import com.edgeatzero.projects.pixiv.ui.common.extras_uuid
import com.edgeatzero.projects.pixiv.util.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IllustrationDetailActivity : BaseActivity<ActivityIllustrationDetailBinding>() {

    override val binding by binding(R.layout.activity_illustration_detail)

    private val uuid by bundleProducer.invokeDelegate(
        key = extras_uuid,
        getter = { getString(it) }
    )
    private val index by bundleProducer.invokeDelegate(
        key = extras_index,
        getter = { getInt(it) }
    )

    private val model by viewModels<IllustrationDetailViewModel>()

    private var illustrations: List<Illustration>? = null
        set(value) {
            if (value != null && field == null) {
                field = value

                binding.ViewPager.adapter = adapter
                if (index > 0) binding.ViewPager.setCurrentItem(index, false)
            }
        }

    private val adapter by lazy {
        object : BaseFragmentStatePagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int) = IllustrationDetailFragment().apply {
                index = position
            }

            override fun getCount() = illustrations?.size ?: 0

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                model.insertHistoryAction(position)
                return super.instantiateItem(container, position)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        delegate.localNightMode = Settings.nightMode
        super.onCreate(savedInstanceState)
        isLayoutFullscreen = true
        statusBarColor = Color.TRANSPARENT

        val data = DataChannel.get<List<Illustration>>(uuid)
        illustrations = data
        data ?: kotlin.run { model.illustrations.observe(this) { illustrations = it } }

        model.postIllustrations(data)
    }

    override fun onRestart() {
        super.onRestart()
        (adapter.current as? IllustrationDetailFragment)?.fullscreen()
    }

    override fun onBackPressed() {
        when {
            (adapter.current as? OnBackPressedListenerFragment)?.onBackPressed() == true -> Unit
            else -> super.onBackPressed()
        }
    }

}
