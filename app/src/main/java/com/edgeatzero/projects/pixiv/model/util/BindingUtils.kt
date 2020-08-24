package com.edgeatzero.projects.pixiv.model.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.os.bundleOf
import androidx.databinding.Observable
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.edgeatzero.library.ext.startActivity
import com.edgeatzero.library.ext.toast
import com.edgeatzero.library.interfaces.BaseAdapterBridge
import com.edgeatzero.library.interfaces.BaseBridge
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.library.model.State
import com.edgeatzero.projects.pixiv.BR
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.model.Commentary
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.model.User
import com.edgeatzero.projects.pixiv.ui.commentary.CommentaryActivity
import com.edgeatzero.projects.pixiv.ui.common.*
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationDetailActivity
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationToggleLike
import com.edgeatzero.projects.pixiv.ui.like.LikeProDialogFragment
import com.edgeatzero.projects.pixiv.ui.novel.NovelToggleLike
import com.edgeatzero.projects.pixiv.ui.ranking.RankingActivity
import com.edgeatzero.projects.pixiv.ui.search.SearchActivity
import com.edgeatzero.projects.pixiv.ui.user.UserToggleFollow
import org.junit.experimental.theories.internal.SpecificDataPointsSupplier
import java.util.*
import kotlin.collections.ArrayList

object BindingUtils {

    fun BaseBridge.commentary(item: Illustration?) {
        item?.let {
            context().startActivity<CommentaryActivity> {
                bundleOf(extras_id to it.id, extras_type to ContentType.ILLUSTRATION)
            }
        }
    }

    fun BaseBridge.commentary(item: Novel?) {
        item?.let {
            context().startActivity<CommentaryActivity> {
                bundleOf(extras_id to it.id, extras_type to ContentType.NOVEL)
            }
        }
    }

    fun BaseBridge.commentaryMenu(item: Commentary?): Boolean {
        item ?: return false
        val manager = context().getSystemService<ClipboardManager>()
        manager ?: return false
        MaterialDialog(context()).show {
            lifecycleOwner(this@commentaryMenu.lifecycleOwner())
            val intent = Intent()
            intent.type = "text/plain"
            intent.component = ComponentName(
                "com.google.android.apps.translate",
                "com.google.android.apps.translate.copydrop.CopyDropContextMenuActivity"
            )
            val enabled = context().packageManager.queryIntentActivities(intent, 0).any()
            listItems(
                res = if (enabled) R.array.list_on_long_click_commentary else R.array.list_on_long_click_commentary_exclude_translate
            ) { _, index, _ ->
                when {
                    index == 0 -> {
                        val clipData = ClipData.newPlainText(null, item.comment)
                        manager.setPrimaryClip(clipData)
                        context.toast { context.getString(R.string.message_copy_successful) }
                    }
                    enabled && index == 1 -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            intent.action = Intent.ACTION_PROCESS_TEXT
                            intent.putExtra(Intent.EXTRA_PROCESS_TEXT, item.comment)
                        } else {
                            intent.action = Intent.ACTION_SEND
                            intent.putExtra(Intent.EXTRA_TEXT, item.comment)
                        }
                        context().startActivity(intent)
                    }
                }
            }
        }
        return true
    }

    @JvmName("detailIllustration")
    fun BaseAdapterBridge<Illustration>.detail(position: Int) {
        return detail(list = current(), position = position)
    }

    @JvmName("detailIllustration")
    fun BaseBridge.detail(item: Illustration) {
        return detail(list = listOf(item), position = 0)
    }

    @JvmName("detailIllustration")
    fun BaseBridge.detail(list: List<Illustration>, position: Int) {
        val uuid = UUID.randomUUID().toString()
        context().startActivity<IllustrationDetailActivity> {
            bundleOf(extras_index to position, extras_uuid to uuid)
        }
        DataChannel[uuid] = list
    }

    @JvmName("detailNovel")
    fun BaseAdapterBridge<Novel>.detail(position: Int) {
        return detail(list = current(), position = position)
    }

    @JvmName("detailNovel")
    fun BaseBridge.detail(item: Novel) {
        return detail(list = listOf(item), position = 0)
    }

    @JvmName("detailNovel")
    fun BaseBridge.detail(list: List<Novel>, position: Int) {
        context().toast { "Not yet implemented" }
    }

    fun BaseBridge.likePro(item: Illustration?): Boolean {
        item ?: return false
        val manager = fragmentManager() ?: return false
        LikeProDialogFragment().apply {
            id = item.id
            type = if (item.isManga) ContentType.MANGA else ContentType.ILLUSTRATION
            tags = ArrayList(item.tags.map { it.name })
        }.show(manager, "likePro")
        return true
    }

    fun BaseBridge.likePro(item: Novel?): Boolean {
        item ?: return false
        val manager = fragmentManager() ?: return false
        LikeProDialogFragment().apply {
            id = item.id
            type = ContentType.NOVEL
            tags = ArrayList(item.tags.map { it.name })
        }.show(manager, "likePro")
        return true
    }

    fun BaseBridge.likePro(item: User?): Boolean {
        item ?: return false
        val manager = fragmentManager() ?: return false
        LikeProDialogFragment().apply {
            id = item.id
            type = ContentType.USER
            tags = null
        }.show(manager, "likePro")
        return true
    }

    fun BaseBridge.rankingDrawable(position: Int): Drawable? = when (position) {
        0 -> ContextCompat.getDrawable(context(), R.drawable.ic_ranking_first)
        1 -> ContextCompat.getDrawable(context(), R.drawable.ic_ranking_second)
        2 -> ContextCompat.getDrawable(context(), R.drawable.ic_ranking_third)
        else -> null
    }

    fun BaseBridge.ranking(
        contentType: ContentType
    ) {
        context().startActivity<RankingActivity> { bundleOf(extras_type to contentType) }
    }

    fun BaseBridge.search(
        word: String,
        contentType: ContentType
    ) {
        context().startActivity<SearchActivity> {
            bundleOf(extras_word to word, extras_type to contentType)
        }
    }

    fun BaseAdapterBridge<Illustration>.toggleLike(
        binding: ViewDataBinding,
        item: Illustration,
        model: IllustrationToggleLike
    ) {
        model.toggleLike(item).observe(lifecycleOwner() ?: return) {
            binding.setVariable(BR.state, it.state)
            processToggleLikeState(item, it)
        }
    }

    fun BaseAdapterBridge<Novel>.toggleLike(
        binding: ViewDataBinding,
        item: Novel,
        model: NovelToggleLike
    ) {
        model.toggleLike(item).observe(lifecycleOwner() ?: return) {
            binding.setVariable(BR.state, it.state)
            processToggleLikeState(item, it)
        }
    }

    fun BaseAdapterBridge<Novel>.toggleFollow(
        binding: ViewDataBinding,
        item: User,
        model: UserToggleFollow
    ) {
        model.toggleFollow(item).observe(lifecycleOwner() ?: return) {
            binding.setVariable(BR.state, it.state)
            processToggleFollowState(item, it)
        }
    }

    private fun BaseBridge.processToggleFollowState(
        data: IsFollowedHolder,
        state: ResponseState
    ) = when (state.state) {
        State.LOADING -> Unit
        State.SUCCESSFUL -> context().toast {
            getString(
                R.string.action_message_success,
                getString(if (data.isFollowed) R.string.message_follow_add else R.string.message_follow_delete)
            )
        }
        State.FAILED -> context().toast {
            getString(
                R.string.action_message_failed,
                getString(if (data.isFollowed) R.string.message_follow_delete else R.string.message_follow_add),
                state.message
            )
        }
    }

    private fun BaseBridge.processToggleLikeState(
        data: IsLikeHolder,
        state: ResponseState
    ) = when (state.state) {
        State.LOADING -> Unit
        State.SUCCESSFUL -> context().toast {
            getString(
                R.string.action_message_success,
                getString(if (data.isLiked) R.string.message_like_add else R.string.message_like_delete)
            )
        }
        State.FAILED -> context().toast {
            getString(
                R.string.action_message_failed,
                getString(if (data.isLiked) R.string.message_like_delete else R.string.message_like_add),
                state.message
            )
        }
    }

    interface IdHolder {

        val id: Long

        override fun equals(other: Any?): Boolean

    }

    fun <T : IdHolder> generate() = object : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

    }

    interface IsFollowedHolder : Observable {

        var isFollowed: Boolean

    }


    interface IsLikeHolder : Observable {

        var isLiked: Boolean
    }

}
