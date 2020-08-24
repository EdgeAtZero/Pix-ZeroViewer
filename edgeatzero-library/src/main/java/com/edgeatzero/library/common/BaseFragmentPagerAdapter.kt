@file:Suppress("DEPRECATION")

package com.edgeatzero.library.common

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

abstract class BaseFragmentPagerAdapter(
    protected val manager: FragmentManager,
    behavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT
) : FragmentPagerAdapter(manager, behavior) {

    protected var container by WeakProperty<ViewPager>()

    protected val context: Context
        get() = requireNotNull(container?.context)

    var current by WeakProperty<Fragment>()
        private set

    operator fun get(position: Int): Fragment? {
        return manager.findFragmentByTag(fragmentName(position))
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        this.container = container as ViewPager?
        return super.instantiateItem(container, position)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        current = `object` as? Fragment
        super.setPrimaryItem(container, position, `object`)
    }

    protected fun fragmentName(
        position: Int
    ): String = "android:switcher:${container?.id}:${getItemId(position)}"

}
