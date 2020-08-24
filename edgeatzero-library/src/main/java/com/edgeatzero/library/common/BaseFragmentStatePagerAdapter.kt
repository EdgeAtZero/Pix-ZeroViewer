@file:Suppress("DEPRECATION", "UNCHECKED_CAST")

package com.edgeatzero.library.common

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

abstract class BaseFragmentStatePagerAdapter(
    protected val manager: FragmentManager,
    behavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT
) : FragmentStatePagerAdapter(manager, behavior) {

    protected var container by WeakProperty<ViewPager>()

    protected val context: Context
        get() = requireNotNull(container?.context)

    var current by WeakProperty<Fragment>()
        private set

    operator fun get(position: Int): Fragment? {
        return getFragments()[position]
    }

    protected fun getFragments(): List<Fragment> {
        val field = FragmentStatePagerAdapter::class.java.getDeclaredField("mFragments")
        field.isAccessible = true
        return field.get(this) as ArrayList<Fragment>
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        this.container = container as ViewPager?
        return super.instantiateItem(container, position)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        current = `object` as? Fragment
        super.setPrimaryItem(container, position, `object`)
    }

}
