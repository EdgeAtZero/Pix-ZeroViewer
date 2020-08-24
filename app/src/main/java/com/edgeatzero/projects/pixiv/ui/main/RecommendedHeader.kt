package com.edgeatzero.projects.pixiv.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.common.SingleItemAdapter
import com.edgeatzero.library.ext.inflate
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.databinding.LayoutRecommendedHeaderBinding
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.ranking
import com.edgeatzero.projects.pixiv.ui.common.LinearItemDecoration
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator

class RecommendedHeader<T>(
    private val type: ContentType,
    creator: () -> ListAdapter<T, out RecyclerView.ViewHolder>
) : SingleItemAdapter<LayoutRecommendedHeaderBinding>() {

    init {
        require(type != ContentType.USER)
    }

    private val adapter by lazy(creator)

    override val display: Boolean
        get() = adapter.itemCount > 0

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = inflater.inflate<LayoutRecommendedHeaderBinding>(container)

    override fun onBindingCreate(binding: LayoutRecommendedHeaderBinding) {
        binding.root.safeUpdateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
            isFullSpan = true
        }
        binding.RecyclerView.apply {
            adapter = this@RecommendedHeader.adapter
            itemAnimator = SlideInRightAnimator()
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            LinearItemDecoration(context).apply {
                replaceTop(px = 0)
                replaceBottom(px = 0)
                margin(res = R.dimen.decoration_margin)
                padding(res = R.dimen.decoration_padding)
                addItemDecoration(this)
            }
        }
        binding.TextViewMore.setOnClickListener { ranking(type) }
    }

    fun submitList(list: List<T>) {
        val display = display
        adapter.submitList(list)
        if (!display) notifyItemInserted(0)
    }

}
