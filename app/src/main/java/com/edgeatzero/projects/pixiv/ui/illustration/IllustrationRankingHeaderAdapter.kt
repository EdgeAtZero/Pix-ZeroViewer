package com.edgeatzero.projects.pixiv.ui.illustration

import android.view.ViewGroup
import com.edgeatzero.library.base.BaseListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.bind
import com.edgeatzero.projects.pixiv.databinding.LayoutIllustrationSmallBinding
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.util.BindingUtils
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.detail
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.rankingDrawable

class IllustrationRankingHeaderAdapter :
    BaseListAdapter<Illustration, DataBindingViewHolder<LayoutIllustrationSmallBinding>>(
        BindingUtils.generate()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = inflateHolder()

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<LayoutIllustrationSmallBinding>,
        position: Int
    ) {
        holder.bind { binding ->
            val item = getItem(position)
            binding.illustration = item
            binding.drawable = rankingDrawable(position)
            binding.root.setOnClickListener { detail(position) }
        }
    }

}
