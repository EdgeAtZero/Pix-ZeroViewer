package com.edgeatzero.projects.pixiv.ui.commentary

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.edgeatzero.library.base.BasePagedListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.bind
import com.edgeatzero.library.ext.startActivity
import com.edgeatzero.projects.pixiv.databinding.LayoutCommentaryBinding
import com.edgeatzero.projects.pixiv.model.Commentary
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.commentaryMenu
import com.edgeatzero.projects.pixiv.ui.common.extras_parent
import com.edgeatzero.projects.pixiv.ui.common.extras_type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentaryAdapter(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null,
    private val model: () -> CommentaryActivity.ViewModel
) : BasePagedListAdapter<Commentary, DataBindingViewHolder<LayoutCommentaryBinding>>(
    Commentary.DIFF_CALLBACK,
    activity,
    fragment
) {

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<LayoutCommentaryBinding>,
        position: Int
    ) {
        holder.bind { binding ->
            val item = getItem(position) ?: return
            binding.root.setOnClickListener { model.invoke().postReply(item) }
            binding.root.setOnLongClickListener { commentaryMenu(item) }
            binding.commentary = item
            binding.TextViewReply.setOnClickListener {
                val model = model.invoke()
                val type = model.contentType.value
                context().startActivity<CommentaryActivity> {
                    bundleOf(extras_parent to item, extras_type to type)
                }
            }
            lifecycleOwner()?.lifecycleScope?.launch(Dispatchers.Main) {
                binding.TextViewCommentary.text = withContext(Dispatchers.Default) { item.span() }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = inflateHolder(parent)

}
