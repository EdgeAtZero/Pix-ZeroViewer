package com.edgeatzero.projects.pixiv.ui.novel

import android.view.ViewGroup
import com.edgeatzero.library.base.BaseListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.bind
import com.edgeatzero.projects.pixiv.databinding.LayoutNovelSmallBinding
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.detail
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.rankingDrawable

class NovelRankingHeaderAdapter :
    BaseListAdapter<Novel, DataBindingViewHolder<LayoutNovelSmallBinding>>(
        Novel.DIFF_CALLBACK
    ) {

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<LayoutNovelSmallBinding>,
        position: Int
    ) {
        holder.bind { binding ->
            val item = getItem(position)
            binding.novel = item
            binding.drawable = rankingDrawable(position)
            binding.root.setOnClickListener { detail(position) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = inflateHolder()

}
