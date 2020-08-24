package com.edgeatzero.projects.pixiv.ui.search

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.base.BaseActivity
import com.edgeatzero.library.behavior.HideBottomViewOnScrollBehavior
import com.edgeatzero.library.common.drawable.FadingDrawable
import com.edgeatzero.library.common.drawable.RotatingDrawable
import com.edgeatzero.library.ext.bundleProducer
import com.edgeatzero.library.ext.invokeDelegate
import com.edgeatzero.library.ext.select
import com.edgeatzero.library.ext.viewModels
import com.edgeatzero.library.util.DisplayUtils
import com.edgeatzero.library.util.KeyboardUtils
import com.edgeatzero.library.view.ExpandableLayout
import com.edgeatzero.library.view.ExpandableLayout.State
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.databinding.ActivitySearchBinding
import com.edgeatzero.projects.pixiv.ui.common.AnimatorAdapter
import com.edgeatzero.projects.pixiv.ui.common.extras_type
import com.edgeatzero.projects.pixiv.ui.common.extras_word
import com.edgeatzero.projects.pixiv.ui.illustration.IllustrationTrendAdapter
import com.edgeatzero.projects.pixiv.util.Settings
import com.google.android.material.tabs.TabLayout
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import org.jetbrains.anko.toast

class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    private val word by bundleProducer.invokeDelegate(
        key = extras_word,
        getter = { getString(it) }
    )

    private val type by bundleProducer.invokeDelegate(
        key = extras_type,
        getter = { getParcelable<ContentType>(it) }
    )

    override val binding by binding(R.layout.activity_search)

    private val model by viewModels<SearchViewModel>()

    private val adapterHolder by lazy { SearchAdapterHolder() }

    private val trendAdapter by lazy { IllustrationTrendAdapter() }

    private val animatorAdapter by lazy { AnimatorAdapter(adapterHolder.adapter) }

    private val expandLayoutHelper by lazy { ExpandLayoutHelper() }

    private val editTextHelper by lazy { EditTextHelper(expandLayoutHelper) }

    private val behavior by lazy { HideBottomViewOnScrollBehavior.from(binding.FloatingActionButton) }

    private val fragment by lazy { SearchResultFragment() }

    private val px by lazy { DisplayUtils.dip2px(6F) }

    override fun onCreate(savedInstanceState: Bundle?) {
        delegate.localNightMode = Settings.nightMode
        super.onCreate(savedInstanceState)
        isLayoutFullscreen = true
        statusBarColor = Color.TRANSPARENT
        isStatusBarLight = true

        binding.MaterialToolbar.apply {
            setSupportActionBar(this)
            setNavigationOnClickListener { onBackPressed() }
        }

        binding.EditText.apply {
            addTextChangedListener(editTextHelper)
            setOnEditorActionListener(editTextHelper)
            onFocusChangeListener = editTextHelper
        }

        binding.RecyclerView.apply {
            adapter = animatorAdapter
            itemAnimator = SlideInDownAnimator()
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
        }

        binding.RecyclerViewHot.apply {
            adapter = trendAdapter
            itemAnimator = SlideInDownAnimator()
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(false)
        }

        binding.TableLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> ContentType.ILLUSTRATION
                    1 -> ContentType.NOVEL
                    2 -> ContentType.USER
                    else -> null
                }?.let { model.postSearchType(it) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })

        binding.ImageButtonClear.setOnClickListener {
            binding.EditText.setText("")
            if (currentFocus == null) KeyboardUtils.showSoftInput(binding.EditText)
        }
        binding.ImageButtonOptions.setImageDrawable(expandLayoutHelper.drawable)
        binding.ImageButtonOptions.setOnClickListener {
            if (expandLayoutHelper.cache.not()) {
                binding.EditText.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
            } else binding.ExpandableLayoutType.toggle()
        }

        binding.ExpandableLayoutType.addOnExpansionUpdateListener(expandLayoutHelper)
        binding.ExpandableLayoutSuggestion.addOnExpansionUpdateListener(editTextHelper)

        binding.FloatingActionButton.setOnClickListener {
            behavior.state = HideBottomViewOnScrollBehavior.STATE_HIDDEN
            fragment.onScrollToTop(false)
        }
        binding.FloatingActionButton.setOnLongClickListener {
            behavior.state = HideBottomViewOnScrollBehavior.STATE_HIDDEN
            fragment.onScrollToTop(true)
            return@setOnLongClickListener true
        }

        trendAdapter.setListener {
            binding.EditText.setText(it)
            val text = binding.EditText.text
            text.select(text.length)
            if (currentFocus == null) KeyboardUtils.showSoftInput(binding.EditText)
        }
        adapterHolder.setListener {
            binding.EditText.setText(it)
            val text = binding.EditText.text
            text.select(text.length)
        }
        adapterHolder.setHeaderListener(model::clearHistories)

        model.searched.observe(this, ::toggle)

        model.loadState.observe(this, binding.include::setState)
        model.loadRetry.observe(this, binding.include::setRetry)

        model.trendTags.observe(this, trendAdapter::submitList)

        model.histories.observe(this) {
            adapterHolder.submitHistories(it, binding.EditText.text.isNullOrBlank())
        }
        model.suggestions.observe(this, adapterHolder::submitSuggestions)

        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentContainerView, fragment)
            .commit()

        val index = when (type) {
            ContentType.NOVEL -> 1
            ContentType.USER -> 2
            else -> 0
        }
        binding.TableLayout.selectTab(binding.TableLayout.getTabAt(index))
        word?.let {
            binding.EditText.setText(it)
            model.postSearchText(it)
            model.action()
        }
    }

    override fun onBackPressed() {
        if (KeyboardUtils.hideSoftInputFromWindow(this)) return
        if (currentFocus?.clearFocus() == Unit) return
        if (model.searched.value == true && fragment.isScrolled) return fragment.onScrollToTop(false)
        if (model.searched.value == true) return kotlin.run {
            binding.ExpandableLayoutType.setExpanded(false)
            model.clearController()
        }
        super.onBackPressed()
    }

    private var cache: Boolean? = null

    private var animator: Animator? = null

    private fun toggle(display: Boolean) {
        if (cache == display) return
        animator?.cancel()
        val animator = AnimatorSet()
        val a0 = ObjectAnimator.ofFloat(
            binding.DrawerLayout,
            View.TRANSLATION_Y,
            binding.DrawerLayout.translationY,
            if (display) 0F else DisplayUtils.getHeight(binding.DrawerLayout.context).toFloat()
        )
        val a1 = ObjectAnimator.ofFloat(
            binding.FrameLayout,
            View.ALPHA,
            binding.FrameLayout.alpha,
            if (display) 0F else 1F
        )
        animator.playTogether(a0, a1)
        animator.duration = 300
        animator.interpolator = FastOutSlowInInterpolator()
        animator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator?) {
                if (display) {
                    binding.DrawerLayout.isVisible = true
                } else {
                    binding.FrameLayout.isVisible = true
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (display) {
                    binding.FrameLayout.isVisible = false
                } else {
                    binding.DrawerLayout.isVisible = false
                }
                this@SearchActivity.animator = null
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}

        })
        this.animator = animator
        animator.start()
        cache = display
    }

    inner class ExpandLayoutHelper(
        @DrawableRes first: Int = R.drawable.ic_expand_more,
        @DrawableRes second: Int = R.drawable.ic_search
    ) : ExpandableLayout.OnExpansionUpdateListener, View.OnFocusChangeListener {

        private val rotatingDrawable by lazy { RotatingDrawable(context(), first) }

        private val fadingDrawable by lazy {
            val s = requireNotNull(ContextCompat.getDrawable(context(), second))
            FadingDrawable(rotatingDrawable, s)
        }

        val drawable: Drawable
            get() = fadingDrawable

        override fun onExpansionUpdate(expansion: Float, oldState: State, newState: State) {
            rotatingDrawable.rotation = expansion.times(180)
            if (binding.ExpandableLayoutSuggestion.isExpanded) return
            binding.AppBarLayout.translationZ = expansion.times(px)
        }

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            toggle(!hasFocus)
        }

        private var animator: Animator? = null

        var cache = true

        private fun toggle(display: Boolean) {
            if (cache == display) return
            cache = display
            animator?.cancel()
            val animator = ObjectAnimator.ofFloat(
                fadingDrawable,
                "fading",
                fadingDrawable.fading,
                if (display) 1F else 0F
            )
            animator.interpolator = FastOutSlowInInterpolator()
            animator.duration = 300
            this.animator = animator
            animator.start()
        }

    }

    inner class EditTextHelper(
        private val listener: View.OnFocusChangeListener
    ) : TextWatcher, TextView.OnEditorActionListener, View.OnFocusChangeListener,
        ExpandableLayout.OnExpansionUpdateListener {

        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (v?.text.isNullOrBlank()) {
                    toast(R.string.tips_cannot_be_empty)
                    return true
                }
                KeyboardUtils.hideSoftInputFromWindow(this@SearchActivity)
                currentFocus?.clearFocus()
                model.action()
                return true
            }
            return false
        }

        override fun onExpansionUpdate(expansion: Float, oldState: State, newState: State) {
            if (binding.ExpandableLayoutType.isExpanded) {
                binding.AppBarLayout.translationZ = px
            } else binding.AppBarLayout.translationZ = expansion.times(px)
        }

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            listener.onFocusChange(v, hasFocus)
            toggleRecyclerView(hasFocus)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            toggleClearButton(s.isNotEmpty())
            model.postSearchText(s.toString())
        }

        private var clearButtonAnimate: ViewPropertyAnimator? = null

        private var clearButtonStateCache = false

        private fun toggleClearButton(display: Boolean) {
            if (clearButtonStateCache == display) return
            clearButtonStateCache = display
            clearButtonAnimate?.cancel()
            val animate = binding.ImageButtonClear.animate()
                .setInterpolator(FastOutSlowInInterpolator())
                .alpha(if (display) 1F else 0F)
                .setListener(object : Animator.AnimatorListener {

                    override fun onAnimationStart(animation: Animator?) {
                        if (display) binding.ImageButtonClear.isVisible = true
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        if (!display) binding.ImageButtonClear.isVisible = false
                        clearButtonAnimate = null
                    }

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationRepeat(animation: Animator?) {}

                })
            this.clearButtonAnimate = animate
            animate.start()
        }

        private var recyclerViewStateCache = false

        private fun toggleRecyclerView(display: Boolean) {
            if (recyclerViewStateCache == display) return
            recyclerViewStateCache = display
            binding.ExpandableLayoutSuggestion.setExpanded(display)
        }

    }

}
