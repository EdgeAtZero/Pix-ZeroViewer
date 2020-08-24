package com.edgeatzero.projects.pixiv.ui.novel

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.base.BindingPagedListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.safeBind
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.projects.pixiv.databinding.LayoutNovelCommonBinding
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.model.util.BindingUtils
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.detail
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.likePro
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.toggleLike
import kotlin.reflect.KClass

open class NovelCommonAdapter @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null,
    private val lambda: () -> NovelToggleLike
) : BindingPagedListAdapter<Novel>(
    BindingUtils.generate(),
    activity,
    fragment
) {

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<out ViewDataBinding>,
        position: Int
    ) {
        holder.safeBind<LayoutNovelCommonBinding> { binding ->
            val item = getItem(position) ?: return
            binding.novel = item
            binding.root.setOnClickListener { detail(position) }
            binding.include.root.setOnClickListener { toggleLike(binding, item, lambda.invoke()) }
            binding.ShapeImageView.setOnLongClickListener { likePro(item.user) }
            binding.include.root.setOnLongClickListener { likePro(item) }
        }
    }

    override fun onInitViewHolder(holder: DataBindingViewHolder<*>, viewType: Int) {
        super.onInitViewHolder(holder, viewType)
        holder.safeBind<LayoutNovelCommonBinding> {binding ->
            binding.root.safeUpdateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
                isFullSpan = true
            }
        }
    }

    override fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding> {
        return LayoutNovelCommonBinding::class
    }

}
