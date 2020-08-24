package com.edgeatzero.projects.pixiv.ui.commentary

import android.text.Editable
import android.view.ViewGroup
import androidx.core.text.toSpannable
import com.edgeatzero.library.base.BaseListAdapter
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.bind
import com.edgeatzero.projects.pixiv.database.EmojiEntity
import com.edgeatzero.projects.pixiv.databinding.LayoutEmojiInputBinding
import com.edgeatzero.projects.pixiv.model.util.EmojiUtils

class CommentaryEmojiAdapter(
    private val maxLength: () -> Int?,
    private val editable: () -> Editable?
) : BaseListAdapter<EmojiEntity, DataBindingViewHolder<LayoutEmojiInputBinding>>(EmojiEntity.DIFF_CALLBACK) {

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<LayoutEmojiInputBinding>,
        position: Int
    ) {
        holder.bind { binding ->
            val item = getItem(position) ?: return
            binding.data = item
            binding.root.setOnClickListener {
                val maxLength = maxLength.invoke() ?: return@setOnClickListener
                val editable = editable.invoke() ?: return@setOnClickListener
                val text = "(${item.definition.slug})".toSpannable()
                if (editable.length + text.length > maxLength) return@setOnClickListener
                EmojiUtils.load(source = text, stream = item.stream)
                editable.append(text)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = inflateHolder(parent)

}
