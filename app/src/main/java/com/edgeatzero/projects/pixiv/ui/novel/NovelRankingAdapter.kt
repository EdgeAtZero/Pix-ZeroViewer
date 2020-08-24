package com.edgeatzero.projects.pixiv.ui.novel

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.safeBind
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.databinding.LayoutNovelLargerBinding
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.detail
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.likePro
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.rankingDrawable
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.toggleLike
import kotlin.reflect.KClass

class NovelRankingAdapter @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null,
    private val lambda: () -> NovelToggleLike
) : NovelCommonAdapter(activity, fragment, lambda) {

    override fun getItemViewType(position: Int): Int {
        return when {
            position < 3 -> R.layout.layout_novel_larger
            else -> super.getItemViewType(position)
        }
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<*>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.safeBind<LayoutNovelLargerBinding> { binding ->
            val item = getItem(position) ?: return
            binding.novel = item
            binding.drawable = rankingDrawable(position)
            binding.root.setOnClickListener { detail(position) }
            binding.include.root.setOnClickListener { toggleLike(binding, item, lambda.invoke()) }
            binding.ShapeImageView.setOnLongClickListener { likePro(item.user) }
            binding.include.root.setOnLongClickListener { likePro(item) }
        }
    }

    override fun onInitViewHolder(holder: DataBindingViewHolder<*>, viewType: Int) {
        super.onInitViewHolder(holder, viewType)
        holder.safeBind<LayoutNovelLargerBinding> { binding ->
            binding.root.safeUpdateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
                isFullSpan = true
            }
        }
    }

    override fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding> {
        return when (viewType) {
            R.layout.layout_novel_larger -> LayoutNovelLargerBinding::class
            else -> super.viewTypeToBindingKClass(viewType)
        }
    }

}
