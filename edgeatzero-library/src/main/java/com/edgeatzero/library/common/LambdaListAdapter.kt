package com.edgeatzero.library.common

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.edgeatzero.library.base.BaseListAdapter

class LambdaListAdapter<T : Any, Binding : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val onBind: (holder: DataBindingViewHolder<Binding>) -> Unit,
    private val onCreate: (parent: ViewGroup) -> Binding
) : BaseListAdapter<T, DataBindingViewHolder<Binding>>(diffCallback) {

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<Binding>,
        position: Int
    ): Unit = onBind.invoke(holder)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = DataBindingViewHolder(onCreate.invoke(parent))

}
