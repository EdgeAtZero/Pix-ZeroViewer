package com.edgeatzero.projects.pixiv.ui.search

import androidx.databinding.ViewDataBinding
import com.edgeatzero.library.base.BindingListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.safeBind
import com.edgeatzero.projects.pixiv.databinding.LayoutSearchSuggestionBinding
import com.edgeatzero.projects.pixiv.model.Tag
import kotlin.reflect.KClass

class SearchSuggestionAdapter : BindingListAdapter<Tag>(Tag.DIFF_CALLBACK) {

    private var block: ((String) -> Unit)? = null

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<out ViewDataBinding>,
        position: Int
    ) {
        holder.safeBind<LayoutSearchSuggestionBinding> { binding ->
            val item = getItem(position)
            binding.data = item
            binding.root.setOnClickListener { block?.invoke(item.name) }
        }
    }

    fun setListener(block: (String) -> Unit) {
        this.block = block
    }

    override fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding> {
        return LayoutSearchSuggestionBinding::class
    }

}
