package com.edgeatzero.projects.pixiv.ui.illustration

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.base.BindingPagedListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.safeBind
import com.edgeatzero.library.util.DisplayUtils
import com.edgeatzero.projects.pixiv.databinding.LayoutIllustrationCommonBinding
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.util.BindingUtils
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.detail
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.likePro
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.toggleLike
import kotlin.math.roundToInt
import kotlin.reflect.KClass

open class IllustrationCommonAdapter @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null,
    protected open val lambda: () -> IllustrationToggleLike
) : BindingPagedListAdapter<Illustration>(
    BindingUtils.generate(),
    activity,
    fragment
) {

    protected open val maxHeight: Double = DisplayUtils.getHeight().times(0.8)

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<out ViewDataBinding>,
        position: Int
    ) {
        holder.safeBind<LayoutIllustrationCommonBinding> { binding ->
            val item = getItem(position) ?: return
            binding.ImageView.updateHeight(binding, item)
            binding.illustration = item
            binding.root.setOnClickListener { detail(position) }
            binding.include.root.setOnClickListener { toggleLike(binding, item, lambda.invoke()) }
            binding.ShapeImageView.setOnLongClickListener { likePro(item.user) }
            binding.include.root.setOnLongClickListener { likePro(item) }
        }
    }

    protected open fun shouldWidth(binding: ViewDataBinding, item: Illustration): Int? {
        val params = binding.root.layoutParams as RecyclerView.LayoutParams
        val isFullSpan = (params as? StaggeredGridLayoutManager.LayoutParams)?.isFullSpan ?: false
        val manager = view?.layoutManager ?: return null
        return if (manager is StaggeredGridLayoutManager && !isFullSpan) {
            DisplayUtils.getWidth(context()).div(manager.spanCount)
        } else DisplayUtils.getWidth(context())
    }

    protected open fun View.updateHeight(binding: ViewDataBinding, item: Illustration) {
        shouldWidth(binding, item)?.let {
            updateLayoutParams { height = it.div(item.ratio).coerceAtMost(maxHeight).roundToInt() }
        }
    }

    override fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding> {
        return LayoutIllustrationCommonBinding::class
    }

}
