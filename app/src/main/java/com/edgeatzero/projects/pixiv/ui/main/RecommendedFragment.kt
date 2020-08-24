package com.edgeatzero.projects.pixiv.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.edgeatzero.library.base.BaseFragment
import com.edgeatzero.library.behavior.HideBottomViewOnScrollBehavior
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.databinding.FragmentRecommendedBinding
import com.edgeatzero.projects.pixiv.ui.common.NotifiableLoadingActionCallback
import com.edgeatzero.projects.pixiv.ui.common.ScrollableCallback
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationRecommendedFragment
import com.edgeatzero.projects.pixiv.ui.manga.MangaRecommendedFragment
import com.edgeatzero.projects.pixiv.ui.novel.NovelRecommendedFragment
import com.edgeatzero.projects.pixiv.util.LambdaFragmentPagerAdapter
import com.edgeatzero.projects.pixiv.util.Settings

class RecommendedFragment : BaseFragment<FragmentRecommendedBinding>() {

    public override val binding by binding()

    private val adapter by lazy {
        LambdaFragmentPagerAdapter(this, count = { 3 }, title = {
            when (it) {
                0 -> this@RecommendedFragment.getString(R.string.title_illustration)
                1 -> this@RecommendedFragment.getString(R.string.title_manga)
                2 -> this@RecommendedFragment.getString(R.string.title_novel)
                else -> throw IndexOutOfBoundsException()
            }
        }, creator = {
            when (it) {
                0 -> IllustrationRecommendedFragment().apply {
                    notifiable = false
                }
                1 -> MangaRecommendedFragment().apply {
                    notifiable = Settings.homeOptimization
                }
                2 -> NovelRecommendedFragment().apply {
                    notifiable = Settings.homeOptimization
                }
                else -> throw IndexOutOfBoundsException()
            }
        })
    }

    private val behavior by lazy { HideBottomViewOnScrollBehavior.from(binding.FloatingActionButton) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity?)?.also { mainActivity ->
            mainActivity.setSupportActionBar(binding.Toolbar)
            val mainBinding = mainActivity.binding
            mainBinding.DrawerLayout.addDrawerListener(ActionBarDrawerToggle(
                mainActivity,
                mainBinding.DrawerLayout,
                binding.Toolbar,
                R.string.app_name,
                R.string.app_name
            ).apply { syncState() })
        }
        binding.ViewPager.apply {
            adapter = this@RecommendedFragment.adapter
            offscreenPageLimit = 2
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
                    val fragment = this@RecommendedFragment.adapter[position] ?: return
                    if (fragment is NotifiableLoadingActionCallback && fragment.isEnableNotifiableLoadingAction && !fragment.isLoaded) fragment.onRequestLoad()
                    if (fragment !is ScrollableCallback) return
                    if (fragment.isScrolled && behavior.state == HideBottomViewOnScrollBehavior.STATE_HIDDEN) {
                        behavior.state = HideBottomViewOnScrollBehavior.STATE_SHOWED
                    } else if (!fragment.isScrolled && behavior.state == HideBottomViewOnScrollBehavior.STATE_SHOWED) {
                        behavior.state = HideBottomViewOnScrollBehavior.STATE_HIDDEN
                    }
                }

            })
        }
        binding.TabLayout.setupWithViewPager(binding.ViewPager)
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
    }

}
