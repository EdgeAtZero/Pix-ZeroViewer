package com.edgeatzero.projects.pixiv.ui.illustration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.common.SingleItemAdapter
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.projects.pixiv.databinding.LayoutIllustrationDetailUserBinding
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.commentary
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.likePro

class IllustrationDetailUser @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null
) : SingleItemAdapter<LayoutIllustrationDetailUserBinding>(activity, fragment) {

    var data: Illustration? = null
        set(value) {
            if (field != value && value != null) {
                field = value
                notifyItemChanged(0)
            }
        }

    private var show: ((Boolean) -> Unit)? = null

    fun showButton(boolean: Boolean) {
        show?.invoke(boolean)
    }

    override fun onBindBinding(binding: LayoutIllustrationDetailUserBinding) {
        super.onBindBinding(binding)
        val data = data ?: return
        binding.data = data
        show = binding::setShowButton
        binding.TextViewCommentary.setOnClickListener { commentary(data) }
        binding.ShapeImageView.setOnLongClickListener { likePro(data.user) }
    }

    override fun onBindingCreate(binding: LayoutIllustrationDetailUserBinding) {
        super.onBindBinding(binding)
        binding.root.safeUpdateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
            isFullSpan = true
        }
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = inflate(container)

}
