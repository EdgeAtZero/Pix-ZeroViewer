package com.edgeatzero.projects.pixiv.ui.search

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.base.BaseFragment
import com.edgeatzero.library.ext.activityViewModels
import com.edgeatzero.library.ext.resolveAttribute
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.databinding.FragmentListBinding
import com.edgeatzero.projects.pixiv.ui.common.LoadStateAdapterImpl
import com.edgeatzero.projects.pixiv.ui.common.ScrollableCallback
import com.edgeatzero.projects.pixiv.ui.common.StaggeredGridItemDecoration
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class SearchResultFragment : BaseFragment<FragmentListBinding>(), ScrollableCallback {

    override val binding by binding()

    private val model by activityViewModels<SearchViewModel>()

    private val adapterHolder by lazy { SearchResultAdapterHolder(this) { model } }

    private val concatAdapter by lazy { ConcatAdapter(adapterHolder.adapter, footer) }

    private val footer by lazy { LoadStateAdapterImpl { retry?.invoke() } }

    private var layoutManager: StaggeredGridLayoutManager? = null

    override val isScrolled: Boolean
        get() {
            val layoutManager = layoutManager ?: return false
            return layoutManager.findFirstCompletelyVisibleItemPositions(null).any { it > 1 }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.RecyclerView.apply {
            adapter = concatAdapter
            itemAnimator = SlideInUpAnimator()
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL).also {
                this@SearchResultFragment.layoutManager = it
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
            setOnRefreshListener { refresh?.invoke() }
            setColorSchemeColors(
                requireContext().resolveAttribute(android.R.attr.colorAccent).data,
                requireContext().resolveAttribute(android.R.attr.colorPrimary).data,
                requireContext().resolveAttribute(android.R.attr.colorPrimaryDark).data
            )
        }
    }

    private var retry: (() -> Unit)? = null

    private var refresh: (() -> Unit)? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        model.state.observe(viewLifecycleOwner) {
            binding.SwipeRefreshLayout.isRefreshing = it.isInitialLoading
            footer.loadState = it
        }
        model.retry.observe(viewLifecycleOwner) { retry = it }
        model.refresh.observe(viewLifecycleOwner) { refresh = it }
        model.illustrations.observe(viewLifecycleOwner, adapterHolder::submitIllustrations)
        model.novels.observe(viewLifecycleOwner, adapterHolder::submitNovels)
        model.user.observe(viewLifecycleOwner, adapterHolder::submitUsers)
    }

    override fun onScrollToTop(fast: Boolean) {
        if (fast) binding.RecyclerView.scrollToPosition(0)
        else binding.RecyclerView.smoothScrollToPosition(0)
    }

}
