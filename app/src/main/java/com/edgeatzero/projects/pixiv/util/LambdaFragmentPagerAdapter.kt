@file:Suppress("DEPRECATION")

package com.edgeatzero.projects.pixiv.util

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.edgeatzero.library.common.BaseFragmentPagerAdapter

open class LambdaFragmentPagerAdapter @JvmOverloads constructor(
    fragmentManager: FragmentManager,
    behavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT,
    private val count: () -> Int,
    private val title: LambdaFragmentPagerAdapter.(position: Int) -> CharSequence? = { null },
    private val creator: (position: Int) -> Fragment,
    protected val onCreated: ((fragment: Fragment) -> Unit)? = null
) : BaseFragmentPagerAdapter(fragmentManager, behavior) {

    constructor(
        activity: FragmentActivity,
        behavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT,
        count: () -> Int,
        title: LambdaFragmentPagerAdapter.(position: Int) -> CharSequence? = { null },
        creator: (position: Int) -> Fragment
    ) : this(activity.supportFragmentManager, behavior, count, title, creator)

    constructor(
        fragment: Fragment,
        behavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT,
        count: () -> Int,
        title: LambdaFragmentPagerAdapter.(position: Int) -> CharSequence? = { null },
        creator: (position: Int) -> Fragment
    ) : this(fragment.childFragmentManager, behavior, count, title, creator)

    override fun getPageTitle(position: Int): CharSequence? {
        return title.invoke(this, position)
    }

    override fun getItem(position: Int): Fragment {
        return creator.invoke(position)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position).also { onCreated?.invoke(it as Fragment) }
    }

    override fun getCount(): Int {
        return count.invoke()
    }

}
