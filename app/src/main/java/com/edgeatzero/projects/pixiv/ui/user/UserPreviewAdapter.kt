package com.edgeatzero.projects.pixiv.ui.user

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.base.BindingPagedListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.safeBind
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.projects.pixiv.databinding.LayoutUserPreviewBinding
import com.edgeatzero.projects.pixiv.model.UserPreview
import com.edgeatzero.projects.pixiv.model.util.BindingUtils
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.likePro
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationToggleLike
import kotlin.reflect.KClass

open class UserPreviewAdapter @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null,
    protected open val lambda: () -> IllustrationToggleLike
) : BindingPagedListAdapter<UserPreview>(
    BindingUtils.generate(),
    activity,
    fragment
) {

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<out ViewDataBinding>,
        position: Int
    ) {
        holder.safeBind<LayoutUserPreviewBinding> { binding ->
            val item = getItem(position) ?: return
            binding.data = item
            binding.ShapeImageView.setOnLongClickListener { likePro(item.user) }
        }
    }

    override fun onInitViewHolder(holder: DataBindingViewHolder<*>, viewType: Int) {
        super.onInitViewHolder(holder, viewType)
        holder.safeBind<LayoutUserPreviewBinding> { binding ->
            binding.root.safeUpdateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
                isFullSpan = true
            }
        }
    }

    override fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding> {
        return LayoutUserPreviewBinding::class
    }

}
