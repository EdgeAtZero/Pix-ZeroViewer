package com.edgeatzero.projects.pixiv.ui.novel

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.edgeatzero.library.base.BaseFragment
import com.edgeatzero.library.ext.activityViewModels
import com.edgeatzero.library.ext.bundleProducer
import com.edgeatzero.library.ext.invokeDelegate
import com.edgeatzero.library.ext.resolveAttribute
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.databinding.FragmentListBinding
import com.edgeatzero.projects.pixiv.event.RefreshEvent
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.ui.common.*
import com.edgeatzero.projects.pixiv.ui.main.RecommendedHeader
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NovelRecommendedFragment : BaseFragment<FragmentListBinding>(
), ScrollableCallback, NotifiableLoadingActionCallback {

    override val binding by binding()

    var notifiable by bundleProducer.invokeDelegate(
        getter = { getBoolean(it) },
        setter = { p0, p1 -> putBoolean(p0, p1) }
    )

    private val model by activityViewModels<ViewModel>()

    private val header by lazy { RecommendedHeader(ContentType.NOVEL) { NovelRankingHeaderAdapter() } }

    private val adapter by lazy { NovelCommonAdapter(fragment = this) { model } }

    private val concatAdapter by lazy { ConcatAdapter(header, AnimatorAdapter(adapter), footer) }

    private val footer by lazy { LoadStateAdapterImpl { model.retry() } }

    private var layoutManager: LinearLayoutManager? = null

    override val isScrolled: Boolean
        get() {
            val layoutManager = layoutManager ?: return false
            return layoutManager.findFirstCompletelyVisibleItemPosition() > 0
        }

    override val isEnableNotifiableLoadingAction
        get() = notifiable

    override val isLoaded: Boolean
        get() = model.isLoaded

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.RecyclerView.apply {
            adapter = concatAdapter
            itemAnimator = SlideInUpAnimator()
            layoutManager = LinearLayoutManager(context).also {
                this@NovelRecommendedFragment.layoutManager = it
            }
            LinearItemDecoration(context).apply {
                adapter(concatAdapter, header, footer)
                margin(res = R.dimen.decoration_margin)
                padding(res = R.dimen.decoration_padding)
                addItemDecoration(this)
            }
            setHasFixedSize(false)
        }
        binding.SwipeRefreshLayout.apply {
            setOnRefreshListener { model.refresh() }
            setColorSchemeColors(
                requireContext().resolveAttribute(android.R.attr.colorAccent).data,
                requireContext().resolveAttribute(android.R.attr.colorPrimary).data,
                requireContext().resolveAttribute(android.R.attr.colorPrimaryDark).data
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.header.observe(viewLifecycleOwner) { header.submitList(it) }
        model.state.observe(viewLifecycleOwner) {
            binding.SwipeRefreshLayout.isRefreshing = it.isInitialLoading
            footer.loadState = it
        }
        model.data.observe(viewLifecycleOwner) { adapter.submitList(it) }
        if (!isEnableNotifiableLoadingAction && !model.isLoaded) model.load()
    }

    override fun onRequestLoad() {
        model.load()
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

    class ViewModel(
        application: Application
    ) : PagingViewModel<Novel>(application) {

        private val original = MutableLiveData<List<Novel>>()
        val header: LiveData<List<Novel>> = original

        override fun load() {
            viewModelScope.launch(Dispatchers.Default) {
                applicationRepository.novelRecommended(original).let {
                    withContext(Dispatchers.Main) { controller.value = it }
                }
            }
        }

    }

}
