package com.edgeatzero.projects.pixiv.ui.commentary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.edgeatzero.library.common.SingleItemAdapter
import com.edgeatzero.library.ext.inflate
import com.edgeatzero.projects.pixiv.databinding.LayoutCommentaryHeaderBinding
import com.edgeatzero.projects.pixiv.model.Commentary
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.commentaryMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentaryHeader(
    private val activity: AppCompatActivity? = null,
    fragment: Fragment? = null
) : SingleItemAdapter<LayoutCommentaryHeaderBinding>(activity, fragment) {

    var parent: Commentary? = null
        set(value) {
            if (field != value && value != null) {
                field = value.copy(hasReplies = false)
                notifyDataSetChanged()
            }
        }

    override val display: Boolean
        get() = parent != null

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = inflater.inflate<LayoutCommentaryHeaderBinding>(container)

    override fun onBindingCreate(binding: LayoutCommentaryHeaderBinding) {
        val item = parent ?: return
        binding.commentary = item
        binding.root.setOnLongClickListener { commentaryMenu(item) }
        lifecycleOwner()?.lifecycleScope?.launch(Dispatchers.Main) {
            binding.include.TextViewCommentary.text = withContext(Dispatchers.Default) {
                item.span()
            }
        }
    }

}
