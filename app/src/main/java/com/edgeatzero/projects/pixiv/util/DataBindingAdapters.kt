package com.edgeatzero.projects.pixiv.util

import android.text.method.LinkMovementMethod
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.sufficientlysecure.htmltextview.HtmlTextView

object DataBindingAdapters {

    @JvmStatic
    var transition: Transition? = Fade()

    @JvmStatic
    @BindingAdapter(requireAll = false, value = ["enabled"])
    fun View.enabled(boolean: Boolean?) {
        if (boolean != isEnabled) isEnabled = boolean ?: return
    }

    @JvmStatic
    @BindingAdapter(requireAll = false, value = ["visible", "translation"])
    fun View.visible(boolean: Boolean?, translation: Boolean?) {
        boolean ?: return
        val parent = parent as? ViewGroup
        parent?.let { TransitionManager.endTransitions(it) }
        if (boolean == isVisible || !boolean == isGone) return
        if (translation == true) parent?.beginDelayedTransition()
        visibility = if (boolean) VISIBLE else GONE
    }

    @JvmStatic
    @BindingAdapter(requireAll = false, value = ["invisible", "translation"])
    fun View.invisible(boolean: Boolean?, translation: Boolean?) {
        boolean ?: return
        val parent = parent as? ViewGroup
        parent?.let { TransitionManager.endTransitions(it) }
        if (boolean == isVisible || !boolean == isInvisible) return
        if (translation == true) parent?.beginDelayedTransition()
        visibility = if (boolean) VISIBLE else INVISIBLE
    }

    @JvmStatic
    inline fun <reified T : ViewGroup> T.beginDelayedTransition(transition: Transition? = this@DataBindingAdapters.transition) {
        TransitionManager.beginDelayedTransition(this, transition)
    }

    @JvmStatic
    @BindingAdapter(requireAll = false, value = ["url"])
    fun ImageView.url(url: String?) {
        url ?: return
        Glide.with(this)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }

    @JvmStatic
    @BindingAdapter(requireAll = false, value = ["html"])
    fun HtmlTextView.html(html: String?) {
        html?.let { setHtml(it) }
    }

    @JvmStatic
    @BindingAdapter(requireAll = false, value = ["text", "setMovementMethod"])
    fun TextView.span(html: CharSequence?, setMovementMethod: Boolean?) {
        html?.let { text = it }
        if (setMovementMethod == true) movementMethod = LinkMovementMethod.getInstance()
    }

//    @JvmStatic
//    @BindingAdapter("app:isUserInputEnabled")
//    fun isUserInputEnabled(view: ViewPager2, boolean: Boolean?) {
//        boolean ?: return
//        view.isUserInputEnabled = boolean
//    }

}