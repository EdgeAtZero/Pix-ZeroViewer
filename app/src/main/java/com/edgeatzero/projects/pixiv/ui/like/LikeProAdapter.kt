package com.edgeatzero.projects.pixiv.ui.like

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.edgeatzero.library.base.BaseListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.bind
import com.edgeatzero.projects.pixiv.databinding.LayoutTagSelectBinding
import com.edgeatzero.projects.pixiv.ui.common.SelectData

class LikeProAdapter(
    private val model: LikeProViewModel
) : BaseListAdapter<SelectData<String>, DataBindingViewHolder<LayoutTagSelectBinding>>(
    object : DiffUtil.ItemCallback<SelectData<String>>() {

        override fun areItemsTheSame(
            oldItem: SelectData<String>,
            newItem: SelectData<String>
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: SelectData<String>,
            newItem: SelectData<String>
        ) = oldItem == newItem

    }
) {

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<LayoutTagSelectBinding>,
        position: Int
    ) {
        holder.bind { binding ->
            val item = getItem(position)
            binding.data = item
            binding.root.setOnClickListener { model.postToggle(item) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = inflateHolder()

}
