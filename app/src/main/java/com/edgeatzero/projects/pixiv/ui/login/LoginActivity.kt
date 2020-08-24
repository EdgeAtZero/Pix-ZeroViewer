package com.edgeatzero.projects.pixiv.ui.login

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.observe
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.edgeatzero.library.base.BaseActivity
import com.edgeatzero.library.ext.startActivity
import com.edgeatzero.library.ext.toast
import com.edgeatzero.library.ext.viewModels
import com.edgeatzero.library.util.KeyboardUtils
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.databinding.ActivityLoginBinding
import com.edgeatzero.projects.pixiv.ui.main.MainActivity
import com.edgeatzero.projects.pixiv.util.LambdaFragmentPagerAdapter
import com.edgeatzero.projects.pixiv.util.Settings

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val binding by binding(R.layout.activity_login)

    private val model by viewModels<LoginViewModel>()

    private val adapter by lazy {
        LambdaFragmentPagerAdapter(this, count = { 2 }, creator = {
            when (it) {
                0 -> LoginFragment()
                1 -> RegisterFragment()
                else -> throw  IndexOutOfBoundsException()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        delegate.localNightMode = Settings.nightMode
        super.onCreate(savedInstanceState)
        isLayoutFullscreen = true
        statusBarColor = Color.TRANSPARENT
        isStatusBarLight = true
        isNavigationBarLight = true

        binding.ScrollableViewPager.apply {
            adapter = this@LoginActivity.adapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager.SCROLL_STATE_DRAGGING) hideKeyboard()
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) = Unit

                override fun onPageSelected(position: Int) {
                    model.postPage(position)
                }

            })
        }

        model.account.observe(this) {
            MaterialDialog(this).show {
                lifecycleOwner(this@LoginActivity)
                cancelOnTouchOutside(false)
                title(R.string.title_register)
                message(text = getString(R.string.message_register, it.account, it.password))
                positiveButton(R.string.positive_ok) {
                    startActivity<MainActivity>()
                    it.dismiss()
                    finishAfterTransition()
                }
            }
        }
        model.state.observe(this) {
            if (it.state.isSuccessful && binding.ScrollableViewPager.currentItem == 0) {
                startActivity<MainActivity>()
                finishAfterTransition()
            }
            if (it.state.isFailed) toast { it.message }
        }

        binding.model = model
    }

    private fun hideKeyboard() {
        KeyboardUtils.hideSoftInputFromWindow(this)
        currentFocus?.clearFocus()
    }

}
