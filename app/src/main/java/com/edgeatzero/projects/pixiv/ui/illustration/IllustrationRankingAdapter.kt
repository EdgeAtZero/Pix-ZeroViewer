package com.edgeatzero.projects.pixiv.ui.illustration

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.safeBind
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.projects.pixiv.databinding.LayoutIllustrationCommonBinding
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.rankingDrawable
import kotlin.reflect.KClass

class IllustrationRankingAdapter @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null,
    lambda: () -> IllustrationToggleLike
) : IllustrationCommonAdapter(activity, fragment, lambda) {

    companion object {

        const val TYPE_FULL_SPAN = 1

    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < 3 -> TYPE_FULL_SPAN
            else -> super.getItemViewType(position)
        }
    }

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<out ViewDataBinding>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        holder.safeBind<LayoutIllustrationCommonBinding> { binding ->
            if (holder.itemViewType == TYPE_FULL_SPAN) binding.drawable = rankingDrawable(position)
        }
    }

    override fun onInitViewHolder(holder: DataBindingViewHolder<*>, viewType: Int) {
        super.onInitViewHolder(holder, viewType)
        if (viewType == TYPE_FULL_SPAN) holder.binding.root.safeUpdateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
            isFullSpan = true
        }
    }

    override fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding> {
        return when (viewType) {
            TYPE_FULL_SPAN -> super.viewTypeToBindingKClass(viewType)
            else -> super.viewTypeToBindingKClass(viewType)
        }
    }

}