package com.edgeatzero.projects.pixiv.ui.search

import androidx.databinding.ViewDataBinding
import com.edgeatzero.library.base.BindingListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.safeBind
import com.edgeatzero.projects.pixiv.database.SearchHistoryEntity
import com.edgeatzero.projects.pixiv.databinding.LayoutSearchHistoryBinding
import kotlin.reflect.KClass

class SearchHistoryAdapter : BindingListAdapter<SearchHistoryEntity>(
    SearchHistoryEntity.DIFF_CALLBACK
) {

    private var block: ((String) -> Unit)? = null

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<out ViewDataBinding>,
        position: Int
    ) {
        holder.safeBind<LayoutSearchHistoryBinding> { binding ->
            val item = getItem(position)
            binding.data = item
            binding.root.setOnClickListener { block?.invoke(item.keyword) }
        }
    }

    fun setListener(block: (String) -> Unit) {
        this.block = block
    }

    override fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding> {
        return LayoutSearchHistoryBinding::class
    }

}

