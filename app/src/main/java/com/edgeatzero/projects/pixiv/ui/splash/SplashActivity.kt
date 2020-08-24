//package com.edgeatzero.projects.pixiv.ui.splash
//
//import android.graphics.Color
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.view.View
//import androidx.transition.TransitionManager
//import com.edgeatzero.library.base.BaseActivity
//import com.edgeatzero.library.ext.isNightMode
//import com.edgeatzero.library.ext.startActivity
//import com.edgeatzero.library.ext.toast
//import com.edgeatzero.projects.pixiv.R
//import com.edgeatzero.projects.pixiv.databinding.ActivitySplashBinding
//import com.edgeatzero.projects.pixiv.ui.login.LoginActivity
//import com.edgeatzero.projects.pixiv.ui.main.MainActivity
//import com.edgeatzero.projects.pixiv.util.Settings
//
//class SplashActivity : BaseActivity<ActivitySplashBinding>() {
//
//    private val handler by lazy { Handler(Looper.getMainLooper()) }
//
//    override val binding by binding(R.layout.activity_splash)
//
//    private val isValidAccount by lazy { Settings.isValidAccount }
//    private var alreadyEnterActivity = false
//    private var paused = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        delegate.localNightMode = Settings.nightMode
//        super.onCreate(savedInstanceState)
//        isLayoutFullscreen = true
//        statusBarColor = Color.TRANSPARENT
//        isStatusBarLight = !isNightMode
//        isNavigationBarLight = !isNightMode
//
//        binding.ConstraintLayout.post {
//            TransitionManager.beginDelayedTransition(binding.ConstraintLayout)
//            binding.TextViewApplicationName.visibility = View.VISIBLE
//            binding.TextViewVersion.visibility = View.VISIBLE
//        }
//
//        if (!isValidAccount) toast { getString(R.string.tips_no_account) }
//        handler.postDelayed(::enterNextActivity, 1500)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        paused = true
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (paused) {
//            paused = false
//            enterNextActivity()
//        }
//    }
//
//    private fun enterNextActivity() {
//        if (alreadyEnterActivity) return
//        if (paused) {
//            return
//        }
//        alreadyEnterActivity = true
//        if (isValidAccount) enterMainActivity()
//        else enterLoginActivity()
//        finishAfterTransition()
//    }
//
//    private fun enterMainActivity() {
//        startActivity<MainActivity>()
//    }
//
//    private fun enterLoginActivity() {
//        startActivity<LoginActivity>()
//    }
//
//}
