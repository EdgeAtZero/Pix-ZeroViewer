package com.edgeatzero.projects.pixiv.ui.illustration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.common.SingleItemAdapter
import com.edgeatzero.library.ext.inflate
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.projects.pixiv.databinding.LayoutIllustrationDetailHeaderBinding
import com.edgeatzero.projects.pixiv.model.Illustration
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IllustrationDetailHeader @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null
) : SingleItemAdapter<LayoutIllustrationDetailHeaderBinding>(activity, fragment) {

    var data: Illustration? = null
        set(value) {
            if (field != value && value != null) {
                field = value
                notifyItemInserted(0)
            }
        }

    override val display: Boolean
        get() = data != null

    private val adapter by lazy { IllustrationTagAdapter(4) }

    override fun onBindBinding(binding: LayoutIllustrationDetailHeaderBinding) {
        val data = data ?: return
        adapter.submitList(data.tags)
        binding.illustration = data
        val lifecycleOwner = lifecycleOwner() ?: return
        binding.lifecycleOwner = lifecycleOwner
        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            binding.span = withContext(Dispatchers.Default) { data.span(context()) }
        }
    }

    override fun onBindingCreate(binding: LayoutIllustrationDetailHeaderBinding) {
        binding.root.safeUpdateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
            isFullSpan = true
        }
        binding.RecyclerViewTag.apply {
            adapter = this@IllustrationDetailHeader.adapter
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }
            setHasFixedSize(true)
        }
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = inflater.inflate<LayoutIllustrationDetailHeaderBinding>(container)

}
