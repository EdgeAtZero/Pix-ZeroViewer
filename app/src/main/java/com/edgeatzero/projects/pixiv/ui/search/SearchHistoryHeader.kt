package com.edgeatzero.projects.pixiv.ui.search

import androidx.databinding.ViewDataBinding
import com.edgeatzero.library.base.BindingAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.projects.pixiv.databinding.LayoutSearchClearBinding
import kotlin.reflect.KClass

class SearchHistoryHeader : BindingAdapter() {

    private var block: (() -> Unit)? = null

    var display: Boolean = false
        set(value) {
            if (field != value) {
                if (field) notifyItemRemoved(0)
                else notifyItemInserted(0)
                field = value
            }
        }

    override fun getItemCount(): Int {
        return if (display) 1 else 0
    }

    fun setListener(block: () -> Unit) {
        this.block = block
    }

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<out ViewDataBinding>,
        position: Int
    ) {
        holder.binding.root.setOnClickListener { block?.invoke() }
    }

    override fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding> {
        return LayoutSearchClearBinding::class
    }


}
