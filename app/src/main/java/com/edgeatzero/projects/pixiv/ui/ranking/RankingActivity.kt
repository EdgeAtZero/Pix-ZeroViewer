package com.edgeatzero.projects.pixiv.ui.ranking

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.library.base.BaseActivity
import com.edgeatzero.library.behavior.HideBottomViewOnScrollBehavior
import com.edgeatzero.library.common.BundleDelegate
import com.edgeatzero.library.ext.bundleProducer
import com.edgeatzero.library.ext.viewModels
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.databinding.ActivityRankingBinding
import com.edgeatzero.projects.pixiv.model.util.DataFormatUtils
import com.edgeatzero.projects.pixiv.ui.common.ScrollableCallback
import com.edgeatzero.projects.pixiv.ui.common.extras_type
import com.edgeatzero.projects.pixiv.util.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RankingActivity : BaseActivity<ActivityRankingBinding>() {

    override val binding by binding(R.layout.activity_ranking)

    private val type by BundleDelegate<ContentType?>(
        bundle = bundleProducer,
        key = extras_type,
        defaultValue = null,
        getter = { p0, p1 -> getParcelable(p0) ?: p1 },
        setter = Bundle::putParcelable
    )

    private val model by viewModels<RankingViewModel>()

    private val adapter by lazy { RankingAdapter(this) }

    private val behavior by lazy { HideBottomViewOnScrollBehavior.from(binding.FloatingActionButton) }

    override fun onCreate(savedInstanceState: Bundle?) {
        delegate.localNightMode = Settings.nightMode
        super.onCreate(savedInstanceState)
        isLayoutFullscreen = true
        statusBarColor = Color.TRANSPARENT

        binding.Toolbar.apply {
            setSupportActionBar(this)
            setNavigationOnClickListener { finishAfterTransition() }
        }
        binding.TabLayout.setupWithViewPager(binding.ViewPager)
        binding.ViewPager.apply {
            adapter = this@RankingActivity.adapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageScrollStateChanged(
                    state: Int
                ) = Unit

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) = Unit

                override fun onPageSelected(position: Int) {
                    val fragment = this@RankingActivity.adapter[position] ?: return
                    if (fragment !is ScrollableCallback) return
                    if (fragment.isScrolled && behavior.state == HideBottomViewOnScrollBehavior.STATE_HIDDEN) {
                        behavior.state = HideBottomViewOnScrollBehavior.STATE_SHOWED
                    } else if (!fragment.isScrolled && behavior.state == HideBottomViewOnScrollBehavior.STATE_SHOWED) {
                        behavior.state = HideBottomViewOnScrollBehavior.STATE_HIDDEN
                    }
                }

            })
        }
        binding.FloatingActionButton.setOnClickListener {
            behavior.state = HideBottomViewOnScrollBehavior.STATE_HIDDEN
            (adapter.current as? ScrollableCallback)?.onScrollToTop(false)
        }
        binding.FloatingActionButton.setOnLongClickListener {
            behavior.state = HideBottomViewOnScrollBehavior.STATE_HIDDEN
            (adapter.current as? ScrollableCallback)?.onScrollToTop(true)
                ?: return@setOnLongClickListener false
            return@setOnLongClickListener true
        }

        model.format.observe(this) {
            binding.Toolbar.title = formatTitle(it)
            model.viewModelScope.launch(Dispatchers.Default) {
                for ((key, value) in model.initializes) {
                    val invoke = value.invoke(it)
                    withContext(Dispatchers.Main) { model.controllers[key]?.value = invoke }
                }
            }
        }
        model.contentType.observe(this) { adapter.submitType(it) }

        type?.let { model.postContentType(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_illustration_ranking, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select_date -> {
                MaterialDialog(this).show {
                    lifecycleOwner(this@RankingActivity)
                    title(res = R.string.title_select_date)
                    datePicker(
                        minDate = DataFormatUtils.rankingStartTimeCalendar,
                        currentDate = model.calendar,
                        maxDate = DataFormatUtils.yesterdayTimeCalendar
                    ) { _, datetime ->
                        model.postDate(datetime.timeInMillis)
                    }
                }
                true
            }
            else -> false
        }
    }

    private fun formatTitle(date: String): String {
        return "${getString(R.string.title_ranking)}${if (date.isNotBlank()) "-$date" else ""}"
    }

}
