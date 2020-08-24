@file:Suppress("DEPRECATION")

package com.edgeatzero.projects.pixiv.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.edgeatzero.library.common.BaseFragmentStatePagerAdapter

open class LambdaFragmentStatePagerAdapter @JvmOverloads constructor(
    fragmentManager: FragmentManager,
    behavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT,
    private val count: () -> Int,
    private val creator: (position: Int) -> Fragment
) : BaseFragmentStatePagerAdapter(fragmentManager, behavior) {

    constructor(
        activity: FragmentActivity,
        behavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT,
        count: () -> Int,
        creator: (position: Int) -> Fragment
    ) : this(activity.supportFragmentManager, behavior, count, creator)

    constructor(
        fragment: Fragment,
        behavior: Int = BEHAVIOR_SET_USER_VISIBLE_HINT,
        count: () -> Int,
        creator: (position: Int) -> Fragment
    ) : this(fragment.childFragmentManager, behavior, count, creator)

    override fun getItem(position: Int): Fragment {
        return creator.invoke(position)
    }

    override fun getCount(): Int {
        return count.invoke()
    }

}
