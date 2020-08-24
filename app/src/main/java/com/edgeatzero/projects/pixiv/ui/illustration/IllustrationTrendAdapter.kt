package com.edgeatzero.projects.pixiv.ui.illustration

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.base.BindingListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.safeBind
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.projects.pixiv.databinding.LayoutIllustrationTrendBinding
import com.edgeatzero.projects.pixiv.model.TrendTag
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.detail
import kotlin.reflect.KClass

class IllustrationTrendAdapter : BindingListAdapter<TrendTag>(TrendTag.DIFF_CALLBACK) {

    companion object {

        const val TYPE_FULL_SPAN = 1

    }

    private var block: ((String) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when {
            position < 1 -> TYPE_FULL_SPAN
            else -> super.getItemViewType(position)
        }
    }

    fun setListener(block: (String) -> Unit) {
        this.block = block
    }

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<out ViewDataBinding>,
        position: Int
    ) {
        holder.safeBind<LayoutIllustrationTrendBinding> { binding ->
            val item = getItem(position) ?: return
            binding.data = item
            binding.first = position == 0
            binding.root.setOnClickListener { block?.invoke(item.tag) }
            binding.root.setOnLongClickListener {
                detail(currentList.map { it.illust }, position)
                true
            }
        }
    }

    override fun onInitViewHolder(holder: DataBindingViewHolder<*>, viewType: Int) {
        super.onInitViewHolder(holder, viewType)
        if (viewType == TYPE_FULL_SPAN) holder.safeBind<LayoutIllustrationTrendBinding> { binding ->
            binding.root.safeUpdateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
                isFullSpan = true
            }
            binding.ImageView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                dimensionRatio = "3:2"
            }
        }
    }

    override fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding> {
        return LayoutIllustrationTrendBinding::class
    }
}
