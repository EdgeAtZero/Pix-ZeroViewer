package com.edgeatzero.projects.pixiv.ui.commentary

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import androidx.core.view.marginEnd
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.*
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.edgeatzero.library.base.BaseActivity
import com.edgeatzero.library.common.drawable.FadingDrawable
import com.edgeatzero.library.ext.*
import com.edgeatzero.library.model.ResponseState
import com.edgeatzero.library.util.HttpMessageConstant.message_data_not_ready
import com.edgeatzero.library.view.SpanEditText.OnTextContextMenuItemListener.DefaultOnTextContextMenuItemListener
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.databinding.ActivityCommentaryBinding
import com.edgeatzero.projects.pixiv.http.repository.AccountRepository
import com.edgeatzero.projects.pixiv.http.suspendExecute
import com.edgeatzero.projects.pixiv.model.Commentary
import com.edgeatzero.projects.pixiv.model.util.EmojiUtils
import com.edgeatzero.projects.pixiv.ui.common.*
import com.edgeatzero.projects.pixiv.util.Settings
import com.effective.android.panel.PanelSwitchHelper
import com.effective.android.panel.view.panel.IPanelView
import com.effective.android.panel.view.panel.PanelView
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import org.kodein.di.generic.instance
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener as OriginalOnPanelChangeListener

class CommentaryActivity : BaseActivity<ActivityCommentaryBinding>() {

    override val binding by binding(R.layout.activity_commentary)

    private val id by bundleProducer.invokeDelegate(
        key = extras_id,
        getter = { getLong(it, -1) }
    )
    private val parent by bundleProducer.invokeDelegate(
        key = extras_parent,
        getter = { getParcelable<Commentary>(it) }
    )
    private val type by bundleProducer.invokeDelegate(
        key = extras_type,
        getter = { getParcelable<ContentType>(it) }
    )

    private val model by viewModels<ViewModel>()

    private val adapter by lazy { CommentaryAdapter(this) { model } }

    private val concatAdapter by lazy { ConcatAdapter(header, AnimatorAdapter(adapter), footer) }

    private val header by lazy { CommentaryHeader(activity = this) }

    private val footer by lazy { LoadStateAdapterImpl { model.retry() } }

    private val fadingDrawable by lazy {
        FadingDrawable(this, R.drawable.ic_emoji_emotions, R.drawable.ic_keyboard)
    }

    private val helper by lazy {
        with(PanelSwitchHelper.Builder(this)) {
            addPanelChangeListener(OnPanelChangeListener())
            addContentScrollMeasurer {
                getScrollDistance { it }
                getScrollViewId { binding.RecyclerView.id }
            }
            contentScrollOutsideEnable(false)
            logTrack(false)
        }.build(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        delegate.localNightMode = Settings.nightMode
        super.onCreate(savedInstanceState)
        isLayoutFullscreen = true
        statusBarColor = Color.TRANSPARENT

        binding.MaterialToolbar.apply {
            setSupportActionBar(this)
            setNavigationOnClickListener { finishAfterTransition() }
        }
        binding.RecyclerView.apply {
            adapter = concatAdapter
            itemAnimator = SlideInUpAnimator()
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
        }

        binding.SpanEditText.addTextChangedListener(afterTextChanged = {
            model.postComment(it?.toString())
        })
        binding.SpanEditText.addOnTextContextMenuItemListener(
            DefaultOnTextContextMenuItemListener(binding.SpanEditText) {
                return@DefaultOnTextContextMenuItemListener EmojiUtils.transform(it, true)
            }
        )

        binding.ImageButtonClear.setOnClickListener {
            model.postReply(null)
            binding.SpanEditText.setText("")
        }
        binding.ImageButtonEmoji.setImageDrawable(fadingDrawable)
        binding.ImageButtonSend.setOnClickListener { toast(message_data_not_ready) }

        binding.SwipeRefreshLayout.apply {
            setOnRefreshListener { model.refresh() }
            setColorSchemeColors(
                resolveAttribute(android.R.attr.colorAccent).data,
                resolveAttribute(android.R.attr.colorPrimary).data,
                resolveAttribute(android.R.attr.colorPrimaryDark).data
            )
        }

        model.state.observe(this) {
            binding.SwipeRefreshLayout.isRefreshing = it.isInitialLoading
            footer.loadState = it
        }
        model.data.observe(this) { adapter.submitList(it) }
        model.parent.observe(this) { header.parent = it }
        model.reply.observe(this) {
            if (it != null && helper.isResetState()) helper.toKeyboardState()
        }
        model.postState.observe(this) {
            if (it.isSuccessful) {
                toast {
                    getString(
                        R.string.action_message_success,
                        getString(R.string.message_comment_add)
                    )
                }
                binding.SpanEditText.setText("")
            } else if (it.isFailed) toast {
                getString(
                    R.string.action_message_failed,
                    getString(R.string.message_comment_add),
                    it.message
                )
            }
        }
        model.isMailAuthorized.observe(this) { isMailAuthorized ->
            binding.ImageButtonSend.setOnClickListener {
                if (isMailAuthorized) model.postAdd()
                else MaterialDialog(this).show {
                    lifecycleOwner(this@CommentaryActivity)
                    message(res = R.string.message_set_email_before_comment)
                    negativeButton(res = android.R.string.cancel) {
                        dismiss()
                    }
                    positiveButton(res = R.string.action_set_email) {
                        toast("Not yet implemented")
                    }
                }
            }
        }

        model.postId(id)
        model.postParent(parent)
        model.postContentType(type)

        binding.model = model
    }

    override fun onStart() {
        super.onStart()
        ::helper.invoke()
    }

    override fun onBackPressed() {
        if (helper.hookSystemBackByPanelSwitcher()) return
        super.onBackPressed()
    }

    inner class OnPanelChangeListener : OriginalOnPanelChangeListener {

        private var boolean: Boolean

        init {
            binding.ConstraintLayoutInfo.visibility = View.GONE
            boolean = true
        }

        private val translationX: Float
            get() = binding.ConstraintLayoutInfo.width + binding.ConstraintLayoutInfo.marginEnd * 1F

        override fun onKeyboard() {
            info(true)
            toggle(false)
        }

        override fun onNone() {
            info(false)
            toggle(false)
        }

        override fun onPanel(panel: IPanelView?) {
            info(true)
            if (panel is PanelView) toggle(panel.id == R.id.PanelView_Emoji)
        }

        private val adapter by lazy {
            CommentaryEmojiAdapter({ model.maxLength.value }, { binding.SpanEditText.text })
        }

        override fun onPanelSizeChange(
            panel: IPanelView?,
            portrait: Boolean,
            oldWidth: Int,
            oldHeight: Int,
            width: Int,
            height: Int
        ) {
            if (panel is PanelView && panel.id == R.id.PanelView_Emoji) {
                val view = panel.findViewById<RecyclerView>(R.id.RecyclerView)
                val size = resources.getDimensionPixelOffset(R.dimen.emoji_input_size)
                val minus = width.minus(view.paddingStart).minus(view.paddingEnd)
                val span = minus.div(size)
                view.adapter = adapter
                view.layoutManager = GridLayoutManager(view.context, span)
                if (adapter.itemCount == 0) adapter.submitList(EmojiUtils.items)
            }
        }

        private var animate: ViewPropertyAnimator? = null

        private fun info(display: Boolean) {
            if (boolean) {
                binding.ConstraintLayoutInfo.translationY = translationX
                binding.ConstraintLayoutInfo.visibility = View.VISIBLE
                boolean = false
            }
            animate?.cancel()
            val animate = binding.ConstraintLayoutInfo.animate()
                .alpha(if (display) 1F else 0F)
                .translationX(if (display) 0F else translationX)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object : Animator.AnimatorListener {

                    override fun onAnimationStart(animation: Animator?) {
                        if (display) binding.ConstraintLayoutInfo.isVisible = true
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        if (!display) binding.ConstraintLayoutInfo.isVisible = false
                        animate == null
                    }

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationRepeat(animation: Animator?) {}

                })
            this.animate = animate
            animate.start()
        }

        private var animator: Animator? = null

        private fun toggle(display: Boolean) {
            animator?.cancel()
            val animator = ObjectAnimator.ofFloat(
                fadingDrawable,
                "fading",
                fadingDrawable.fading,
                if (display) 0F else 1F
            )
            animator.duration = 300
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.addListener(onEnd = { this.animator == null })
            this.animator = animator
            animator.start()
        }

    }

    class ViewModel(
        application: Application,
        handle: SavedStateHandle
    ) : PagingViewModel<Commentary>(application) {

        companion object {

            const val KEY_ID = "key_id"
            const val KEY_PARENT = "key_parent"
            const val KEY_CONTENT_TYPE = "key_content_type"

        }

        private val accountRepository by instance<AccountRepository>()

        private val id = handle.getLiveData<Long>(KEY_ID)

        private val _parent = handle.getLiveData<Commentary>(KEY_PARENT)
        val parent: LiveData<Commentary> = _parent

        private val _contentType = handle.getLiveData<ContentType>(KEY_CONTENT_TYPE)
        val contentType: LiveData<ContentType> = _contentType

        val isMailAuthorized = accountRepository.currentAccountAsLiveData.map {
            it.user.isMailAuthorized
        }

        private val _maxLength = MutableLiveData(140)
        val maxLength: LiveData<Int> = _maxLength

        private val _reply = MutableLiveData<Commentary?>(null)
        val reply: LiveData<Commentary?> = _reply

        private val _comment = MutableLiveData<String>("")
        val comment: LiveData<String> = _comment

        private val _postState = MutableLiveData<ResponseState>()
        val postState: LiveData<ResponseState> = _postState

        init {
            controller.addSource(id) { load(it, _contentType.value, _parent.value) }
            controller.addSource(_parent) { load(id.value, _contentType.value, it) }
            controller.addSource(_contentType) { load(id.value, it, _parent.value) }
        }

        fun load(id: Long?, type: ContentType?, parent: Commentary? = null) {
            if (id == null || type == null) return
            viewModelScope.launch(Dispatchers.Default) {
                val value = if (parent == null) when (type) {
                    ContentType.ILLUSTRATION, ContentType.MANGA -> {
                        applicationRepository.illustrationCommentaries(id)
                    }
                    ContentType.NOVEL -> {
                        applicationRepository.novelCommentaries(id)
                    }
                    else -> throw IndexOutOfBoundsException()
                } else when (type) {
                    ContentType.ILLUSTRATION, ContentType.MANGA -> {
                        applicationRepository.illustrationCommentaryReplies(parent.id)
                    }
                    ContentType.NOVEL -> {
                        applicationRepository.novelCommentaryReplies(parent.id)
                    }
                    else -> throw IndexOutOfBoundsException()
                }
                withContext(Dispatchers.Main) { controller.value = value }
            }
        }

        fun postId(id: Long) {
            this.id.setIfDifferent(id)
        }

        fun postParent(parent: Commentary?) {
            _parent.setIfDifferent(parent)
        }

        fun postContentType(contentType: ContentType?) {
            _contentType.setIfDifferent(contentType)
        }

        fun postReply(commentary: Commentary?) {
            _reply.value = commentary
        }

        fun postComment(text: String?) {
            _comment.setIfDifferent(text)
        }

        fun postAdd() {
            viewModelScope.launch(Dispatchers.Default) {
                if (_postState.value?.isLoading == true) return@launch
                withContext(Dispatchers.Main) { _postState.value = ResponseState.LOADING }
                val id = id.value
                val type = _contentType.value
                val comment = _comment.value
                val parent = _reply.value ?: _parent.value
                if (id == null || type == null || comment == null) {
                    _postState.value = ResponseState.failed(message_data_not_ready)
                    return@launch
                }
                when (type) {
                    ContentType.ILLUSTRATION, ContentType.MANGA -> {
                        applicationRepository.illustrationCommentaryAdd(id, comment, parent?.id)
                    }
                    ContentType.NOVEL -> {
                        applicationRepository.novelCommentaryAdd(id, comment, parent?.id)
                    }
                    else -> throw IndexOutOfBoundsException()
                }.suspendExecute(
                    onSuccessful = { _ ->
                        withContext(Dispatchers.Main) {
                            refresh()
                            _postState.value = ResponseState.SUCCESSFUL
                        }
                    }, onFailed = { message, body ->
                        withContext(Dispatchers.Main) {
                            _postState.value = ResponseState.failed(body?.errorMessage ?: message)
                        }
                    }
                )
            }
        }

        fun postDelete(id: Long) =
            viewModelScope.launchAsLiveData<ResponseState>(Dispatchers.Default) {
                withContext(Dispatchers.Main) { it.value = ResponseState.LOADING }
                val type = _contentType.value ?: kotlin.run {
                    it.value = ResponseState.failed(message_data_not_ready)
                    return@launchAsLiveData
                }
                when (type) {
                    ContentType.ILLUSTRATION, ContentType.MANGA -> {
                        applicationRepository.illustrationCommentaryDelete(id)
                    }
                    ContentType.NOVEL -> {
                        applicationRepository.illustrationCommentaryDelete(id)
                    }
                    else -> throw IndexOutOfBoundsException()
                }.suspendExecute(
                    onSuccessful = { _ ->
                        withContext(Dispatchers.Main) {
                            refresh()
                            it.value = ResponseState.SUCCESSFUL
                        }
                    }, onFailed = { message, body ->
                        withContext(Dispatchers.Main) {
                            it.value = ResponseState.failed(body?.errorMessage ?: message)
                        }
                    }
                )
            }

    }

}
