package com.edgeatzero.projects.pixiv.ui.illustration

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.edgeatzero.library.base.BindingListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.safeBind
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.library.util.DisplayUtils
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.databinding.LayoutTagCommonBinding
import com.edgeatzero.projects.pixiv.model.Tag
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.search
import kotlin.reflect.KClass

class IllustrationTagAdapter(
    private val dip: Int = 8,
    private val contentType: ContentType = ContentType.ILLUSTRATION
) : BindingListAdapter<Tag>(Tag.DIFF_CALLBACK) {

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<out ViewDataBinding>,
        position: Int
    ) {
        holder.safeBind<LayoutTagCommonBinding> { binding ->
            val item = getItem(position) ?: return
            binding.data = item
            binding.root.setOnClickListener { search(item.name, contentType) }
        }
    }

    override fun onInitViewHolder(holder: DataBindingViewHolder<*>, viewType: Int) {
        super.onInitViewHolder(holder, viewType)
        holder.safeBind<LayoutTagCommonBinding> { binding ->
            binding.root.safeUpdateLayoutParams<ViewGroup.MarginLayoutParams> {
                val size = DisplayUtils.dip2px(dip)
                setMargins(size, 0, size, 0)
            }
        }
    }

    override fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding> {
        return LayoutTagCommonBinding::class
    }

}
