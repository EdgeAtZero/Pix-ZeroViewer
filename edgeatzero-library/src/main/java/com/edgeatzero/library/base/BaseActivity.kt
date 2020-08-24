package com.edgeatzero.library.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.edgeatzero.library.ext.addSystemUiVisibilityFlag
import com.edgeatzero.library.ext.includeSystemUiVisibilityFlag
import com.edgeatzero.library.ext.inflate
import com.edgeatzero.library.ext.removeSystemUiVisibilityFlag
import com.edgeatzero.library.interfaces.BaseBridge

abstract class BaseActivity<T : ViewDataBinding> : InjectionActivity(), BaseBridge {

    var statusBarColor: Int
        get() = window.statusBarColor
        set(value) {
            window.statusBarColor = value
        }

    var isFullscreen: Boolean
        get() = window.decorView.includeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_FULLSCREEN)
        set(value) {
            if (value) window.decorView.addSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_FULLSCREEN)
            else window.decorView.removeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_FULLSCREEN)
        }

    var isHideNavigation
        get() = window.decorView.includeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        set(value) {
            if (value) window.decorView.addSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            else window.decorView.removeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_FULLSCREEN)
        }

    var isImmersiveSitcky: Boolean
        get() = window.decorView.includeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_FULLSCREEN)
        set(value) {
            if (value) window.decorView.addSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            else window.decorView.removeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

    var isLayoutFullscreen: Boolean
        get() = window.decorView.includeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        set(value) {
            if (value) window.decorView.addSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            else window.decorView.removeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }

    var isNavigationBarLight: Boolean
        @RequiresApi(Build.VERSION_CODES.O)
        get() = window.decorView.includeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        set(value) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
            if (value) window.decorView.addSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
            else window.decorView.removeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        }

    var isStatusBarLight: Boolean
        @RequiresApi(Build.VERSION_CODES.M)
        get() = window.decorView.includeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        set(value) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
            if (value) window.decorView.addSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            else window.decorView.removeSystemUiVisibilityFlag(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

    protected open val enable = true

    protected abstract val binding: T

    override fun context(): Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (enable) binding.lifecycleOwner = this
    }

    companion object {

        @JvmStatic
        inline fun <reified T : ViewDataBinding> BaseActivity<*>.inflate(
            container: ViewGroup? = null
        ): T = layoutInflater.inflate(container)

        @JvmStatic
        fun <T : ViewDataBinding> BaseActivity<T>.binding(
            @LayoutRes layoutId: Int
        ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, layoutId) }

    }

}
