package com.edgeatzero.projects.pixiv.ui.illustration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.observe
import androidx.lifecycle.switchMap
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.base.BaseActivity
import com.edgeatzero.library.base.BaseFragment
import com.edgeatzero.library.behavior.HideBottomViewOnScrollBehavior
import com.edgeatzero.library.ext.activityViewModels
import com.edgeatzero.library.ext.bundleProducer
import com.edgeatzero.library.ext.invokeDelegate
import com.edgeatzero.library.ext.toast
import com.edgeatzero.library.model.State
import com.edgeatzero.library.util.DisplayUtils
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.databinding.FragmentIllustrationDetailBinding
import com.edgeatzero.projects.pixiv.event.RefreshEvent
import com.edgeatzero.projects.pixiv.http.repository.ApplicationRepository
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.util.BindingUtils.likePro
import com.edgeatzero.projects.pixiv.model.util.PixivUtils.share
import com.edgeatzero.projects.pixiv.ui.common.AnimatorAdapter
import com.edgeatzero.projects.pixiv.ui.common.LoadStateAdapterImpl
import com.edgeatzero.projects.pixiv.ui.common.OnBackPressedListenerFragment
import com.edgeatzero.projects.pixiv.ui.common.StaggeredGridItemDecoration
import com.edgeatzero.projects.pixiv.ui.like.LikeProDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.kodein.di.generic.instance

class IllustrationDetailFragment : BaseFragment<FragmentIllustrationDetailBinding>(
), OnBackPressedListenerFragment, LikeProDialogFragment.OnDismissListener {

    override val binding by binding()

    var index by bundleProducer.invokeDelegate(
        key = "index",
        getter = { getInt(it) },
        setter = { p0, p1 -> putInt(p0, p1) }
    )

    val illustration
        get() = model.getIllustration(index)

    private var need: Boolean = false

    private val model by activityViewModels<IllustrationDetailViewModel>()

    private val applicationRepository by instance<ApplicationRepository>()

    private val adapter by lazy { IllustrationSingleAdapter(fragment = this) }

    private val header by lazy { IllustrationDetailHeader(fragment = this) }

    private val user by lazy { IllustrationDetailUser(fragment = this) }

    private val relatedAdapter by lazy { IllustrationCommonAdapter(fragment = this) { model } }

    private val layoutManager by lazy { StaggeredGridLayoutManager(2, RecyclerView.VERTICAL) }

    private val animatorAdapter by lazy { AnimatorAdapter(relatedAdapter) }

    private val concatAdapter by lazy { ConcatAdapter(user, header, animatorAdapter, footer) }

    private val footer by lazy { LoadStateAdapterImpl(showInitialLoad = true) { model.retry(index) } }

    private val behavior by lazy { BottomSheetBehavior.from(binding.RecyclerViewBottom) }

    private val buttonBehavior by lazy { HideBottomViewOnScrollBehavior.from(binding.FloatingActionButtonTOP) }

    override fun fragmentManager(): FragmentManager = childFragmentManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.Toolbar.apply {
            setNavigationOnClickListener { requireActivity().finishAfterTransition() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.share -> {
                        requireContext().share(illustration)
                        true
                    }
                    else -> false
                }
            }
        }
        binding.RecyclerView.apply {
            adapter = this@IllustrationDetailFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.RecyclerViewBottom.apply {
            adapter = concatAdapter
            itemAnimator = SlideInUpAnimator()
            layoutManager = this@IllustrationDetailFragment.layoutManager
            StaggeredGridItemDecoration(context).apply {
                adapter(concatAdapter, { user.itemCount + header.itemCount }, { footer.itemCount })
                extra(px = 0)
                margin(res = R.dimen.decoration_margin)
                padding(res = R.dimen.decoration_padding)
                addItemDecoration(this)
            }
            setHasFixedSize(false)
        }
        binding.FloatingActionButton.setOnClickListener {
            val item = illustration ?: return@setOnClickListener
            model.toggleLike(item).observe(viewLifecycleOwner) {
                binding.loading = it.isLoading
                when (it.state) {
                    State.SUCCESSFUL -> requireContext().toast {
                        getString(
                            R.string.action_message_success,
                            getString(if (item.isLiked) R.string.message_like_add else R.string.message_like_delete)
                        )
                    }
                    State.FAILED -> requireContext().toast {
                        getString(
                            R.string.action_message_failed,
                            getString(if (item.isLiked) R.string.message_like_delete else R.string.message_like_add),
                            it.message
                        )
                    }
                    else -> Unit
                }
            }
        }
        binding.FloatingActionButton.setOnLongClickListener { likePro(illustration) }
        binding.FloatingActionButtonTOP.setOnClickListener {
            binding.RecyclerViewBottom.smoothScrollToPosition(0)
            buttonBehavior.state = HideBottomViewOnScrollBehavior.STATE_HIDDEN
        }
        binding.FloatingActionButtonTOP.setOnLongClickListener {
            binding.RecyclerViewBottom.scrollToPosition(0)
            buttonBehavior.state = HideBottomViewOnScrollBehavior.STATE_HIDDEN
            return@setOnLongClickListener true
        }
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            val deltaY: Int
                get() = binding.RecyclerViewBottom.height - DisplayUtils.dip2px(dip = 67)

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.RecyclerView.translationY = -deltaY * slideOffset * 0.618f
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                need = if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    if (activity != null && !model.isLoad(index)) {
                        header.data = illustration
                        model.load(index)
                    }
                    fullscreen(true)
                    user.showButton(true)
                    true
                } else {
                    fullscreen(false)
                    user.showButton(false)
                    false
                }
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val illustration = illustration ?: return
        binding.illustration = illustration
        adapter.data = illustration
        user.data = illustration
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) header.data = illustration
        val controller = model.getController<Illustration>(index, false) {
            applicationRepository.illustrationRelated(illustration.id)
        } ?: return
        controller.switchMap { it.data }.observe(viewLifecycleOwner) {
            relatedAdapter.submitList(it)
        }
        controller.switchMap { it.state.invoke() }.observe(viewLifecycleOwner) {
            footer.loadState = it
        }
    }

    override fun onDismiss(): Boolean {
        fullscreen()
        return true
    }

    fun fullscreen(boolean: Boolean = need) {
        val activity = activity as? BaseActivity<*> ?: return
        activity.isFullscreen = boolean
        activity.isHideNavigation = boolean
        activity.isImmersiveSitcky = boolean
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onEvent(event: RefreshEvent) {
        val value = illustration ?: return
        when (event.contentType) {
            ContentType.ILLUSTRATION, ContentType.MANGA -> {
                if (value.id == event.id) value.isLiked = event.boolean
            }
            ContentType.NOVEL -> Unit
            ContentType.USER ->
                if (value.user.id == event.id) value.user.isFollowed = event.boolean

        }
    }

    override fun onBackPressed(): Boolean {
        return when {
            layoutManager.findFirstCompletelyVisibleItemPositions(null).any { it > 0 } -> {
                buttonBehavior.state = HideBottomViewOnScrollBehavior.STATE_HIDDEN
                binding.RecyclerViewBottom.smoothScrollToPosition(0)
                true
            }
            behavior.state == BottomSheetBehavior.STATE_EXPANDED -> {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                true
            }
            else -> false
        }
    }

}
