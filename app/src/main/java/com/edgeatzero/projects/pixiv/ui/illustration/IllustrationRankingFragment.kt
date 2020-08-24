package com.edgeatzero.projects.pixiv.ui.illustration

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.lifecycle.switchMap
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.ui.common.AnimatorAdapter
import com.edgeatzero.projects.pixiv.ui.common.LoadStateAdapterImpl
import com.edgeatzero.projects.pixiv.ui.common.ScrollableCallback
import com.edgeatzero.projects.pixiv.ui.common.StaggeredGridItemDecoration
import com.edgeatzero.projects.pixiv.ui.ranking.RankingViewModel
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.kodein.di.generic.instance

class IllustrationRankingFragment : BaseFragment<FragmentListBinding>(), ScrollableCallback {

    override val binding by binding()

    var category by bundleProducer.invokeDelegate(
        getter = { getParcelable<RankingCategory>(it) },
        setter = Bundle::putParcelable
    )

    private val model by activityViewModels<RankingViewModel>()

    private val applicationRepository by instance<ApplicationRepository>()

    private val adapter by lazy { IllustrationRankingAdapter(fragment = this) { model } }

    private val concatAdapter by lazy { ConcatAdapter(AnimatorAdapter(adapter), footer) }

    private val footer by lazy { LoadStateAdapterImpl { model.retry(category) } }

    private var layoutManager: StaggeredGridLayoutManager? = null

    override val isScrolled: Boolean
        get() {
            val layoutManager = layoutManager ?: return false
            return layoutManager.findFirstCompletelyVisibleItemPositions(null).any { it > 0 }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.RecyclerView.apply {
            adapter = concatAdapter
            itemAnimator = SlideInUpAnimator()
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL).also {
                this@IllustrationRankingFragment.layoutManager = it
            }
            StaggeredGridItemDecoration(context).apply {
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
        val controller = model.getController<Illustration>(category, true) {
            applicationRepository.illustrationRanking(category, it)
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
            ContentType.ILLUSTRATION, ContentType.MANGA -> {
                val illustration = adapter.currentList?.firstOrNull { it.id == event.id }
                illustration?.isLiked = event.boolean
            }
            ContentType.NOVEL -> Unit
            ContentType.USER -> {
                val illustration = adapter.currentList?.firstOrNull { it.user.id == event.id }
                illustration?.user?.isFollowed = event.boolean
            }

        }
    }

}
