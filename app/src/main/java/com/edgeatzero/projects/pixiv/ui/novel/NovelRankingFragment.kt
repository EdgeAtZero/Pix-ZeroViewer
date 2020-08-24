package com.edgeatzero.projects.pixiv.ui.novel

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.lifecycle.switchMap
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.edgeatzero.library.base.BaseFragment
import com.edgeatzero.library.ext.activityViewModels
import com.edgeatzero.library.ext.bundleProducer
import com.edgeatzero.library.ext.invokeDelegate
import com.edgeatzero.library.ext.resolveAttribute
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.constant.RankingCategory
import com.edgeatzero.projects.pixiv.databinding.FragmentListBinding
import com.edgeatzero.projects.pixiv.event.RefreshEvent
import com.edgeatzero.projects.pixiv.http.repository.ApplicationRepository
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.ui.common.AnimatorAdapter
import com.edgeatzero.projects.pixiv.ui.common.LinearItemDecoration
import com.edgeatzero.projects.pixiv.ui.common.LoadStateAdapterImpl
import com.edgeatzero.projects.pixiv.ui.common.ScrollableCallback
import com.edgeatzero.projects.pixiv.ui.ranking.RankingViewModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.kodein.di.generic.instance

class NovelRankingFragment : BaseFragment<FragmentListBinding>(
), ScrollableCallback {

    override val binding by binding()

    var category by bundleProducer.invokeDelegate(
        getter = { getParcelable<RankingCategory>(it) },
        setter = Bundle::putParcelable
    )

    private val model by activityViewModels<RankingViewModel>()

    private val applicationRepository by instance<ApplicationRepository>()

    private val adapter by lazy { NovelRankingAdapter(fragment = this) { model } }

    private val concatAdapter by lazy { ConcatAdapter(AnimatorAdapter(adapter), footer) }

    private val footer by lazy { LoadStateAdapterImpl { model.retry(category) } }

    private var layoutManager: LinearLayoutManager? = null

    override val isScrolled: Boolean
        get() {
            val layoutManager = layoutManager ?: return false
            return layoutManager.findFirstCompletelyVisibleItemPosition() > 0
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.RecyclerView.apply {
            adapter = concatAdapter
            itemAnimator = SlideInUpAnimator()
            layoutManager = LinearLayoutManager(context).also {
                this@NovelRankingFragment.layoutManager = it
            }
            LinearItemDecoration(context).apply {
                adapter(concatAdapter, null, footer)
                margin(res = R.dimen.decoration_margin)
                padding(res = R.dimen.decoration_padding)
                addItemDecoration(this)
            }
            setHasFixedSize(false)
        }
        binding.SwipeRefreshLayout.apply {
            setOnRefreshListener { model.refresh(category) }
            setColorSchemeColors(
                requireContext().resolveAttribute(android.R.attr.colorAccent).data,
                requireContext().resolveAttribute(android.R.attr.colorPrimary).data,
                requireContext().resolveAttribute(android.R.attr.colorPrimaryDark).data
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val category = category ?: return
        val controller = model.getController<Novel>(category, true) {
            applicationRepository.novelRanking(category, it)
        } ?: return
        controller.switchMap { it.data }.observe(viewLifecycleOwner) { adapter.submitList(it) }
        controller.switchMap { it.state.invoke() }.observe(viewLifecycleOwner) {
            binding.SwipeRefreshLayout.isRefreshing = it.isInitialLoading
            footer.loadState = it
        }
    }

    override fun onScrollToTop(fast: Boolean) {
        if (fast) binding.RecyclerView.scrollToPosition(0)
        else binding.RecyclerView.smoothScrollToPosition(0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEvent(event: RefreshEvent) {
        when (event.contentType) {
            ContentType.ILLUSTRATION, ContentType.MANGA -> Unit
            ContentType.NOVEL -> {
                val novel = adapter.currentList?.firstOrNull { it.id == event.id }
                novel?.isLiked = event.boolean
            }
            ContentType.USER -> {
                val novel = adapter.currentList?.firstOrNull { it.user.id == event.id }
                novel?.user?.isFollowed = event.boolean
            }

        }
    }

}
