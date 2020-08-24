package com.edgeatzero.projects.pixiv.ui.like

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.edgeatzero.library.base.BaseDialogFragment
import com.edgeatzero.library.ext.bundleProducer
import com.edgeatzero.library.ext.invokeDelegate
import com.edgeatzero.library.ext.toast
import com.edgeatzero.library.ext.updateAttributes
import com.edgeatzero.library.model.State
import com.edgeatzero.library.util.DisplayUtils
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.databinding.FragmentDialogLikeProBinding
import com.edgeatzero.projects.pixiv.ui.common.AnimatorAdapter
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator

class LikeProDialogFragment : BaseDialogFragment<FragmentDialogLikeProBinding>() {

    override val binding by binding()

    var id by bundleProducer.invokeDelegate(
        getter = { getLong(it, -1) },
        setter = Bundle::putLong
    )
    var type by bundleProducer.invokeDelegate(
        getter = { getParcelable<ContentType>(it) },
        setter = Bundle::putParcelable
    )
    var tags by bundleProducer.invokeDelegate(
        getter = Bundle::getStringArrayList,
        setter = Bundle::putStringArrayList
    )

    private val model by viewModels<LikeProViewModel>()

    private val adapter by lazy { LikeProAdapter(model) }

    private val animatorAdapter by lazy { AnimatorAdapter(adapter) }

    private val callback by lazy { LikeProItemTouchCallback(model, adapter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.MaterialToolbar.setNavigationOnClickListener { dismiss() }
        binding.RecyclerView.apply {
            adapter = animatorAdapter
            itemAnimator = SlideInDownAnimator()
            layoutManager = LinearLayoutManager(context)
            ItemTouchHelper(callback).attachToRecyclerView(this)
            setHasFixedSize(false)
        }
        binding.ImageButton.setOnClickListener {
            if (model.postTag(binding.EditText.text.toString())) {
                binding.EditText.text.clear()
                adapter.notifyItemInserted(0)
                if (model.isUser.value != true) binding.RecyclerView.scrollToPosition(0)
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.model = model
        model.likeState.observe(this) {
            val actionLike = if (model.isUser.value == true) {
                if (model.toLiked) R.string.message_follow_add else R.string.message_follow_delete
            } else {
                if (model.toLiked) R.string.message_like_add else R.string.message_like_delete
            }
            when (it.state) {
                State.SUCCESSFUL -> {
                    requireContext().toast {
                        getString(R.string.action_message_success, getString(actionLike))
                    }
                    dismiss()
                }
                State.FAILED -> {
                    requireContext().toast {
                        getString(R.string.action_message_failed, getString(actionLike), it.message)
                    }
                }
                else -> Unit
            }
        }
        model.loadState.observe(this) {
            if (it.isSuccessful) dialog?.window?.updateAttributes {
                if (model.isUser.value == true) binding.CoordinatorLayout.updateLayoutParams<ViewGroup.LayoutParams> {
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                } else height = DisplayUtils.getHeight().minus(DisplayUtils.dip2px(24).times(2))
            }
        }
        model.tags.observe(viewLifecycleOwner)
        { adapter.submitList(it) }
        model.postData(id, type, tags)

        dialog?.apply {
            setCanceledOnTouchOutside(false)
            window?.updateAttributes {
                width = DisplayUtils.getWidth().minus(DisplayUtils.dip2px(24).times(2))
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if ((activity as? OnDismissListener)?.onDismiss() == true) return
        if ((parentFragment as? OnDismissListener)?.onDismiss() == true) return
    }

    interface OnDismissListener {

        fun onDismiss(): Boolean

    }

}
