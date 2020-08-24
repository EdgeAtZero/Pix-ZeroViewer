//@file:Suppress("DEPRECATION")
//
//package com.edgeatzero.library.ext
//
//import android.content.Context
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentPagerAdapter
//import com.edgeatzero.library.interfaces.TitleHolder
//
//fun FragmentActivity.fragmentPagerAdapter(
//    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
//    fragment: () -> List<Fragment>
//): FragmentPagerAdapter = fragmentPagerAdapter(
//    context = this,
//    fragmentManager = supportFragmentManager,
//    fragment = fragment,
//    behavior = behavior
//)
//
//fun FragmentActivity.fragmentPagerAdapterLazy(
//    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
//    fragment: () -> List<Fragment>
//): Lazy<FragmentPagerAdapter> = lazy {
//    fragmentPagerAdapter(
//        context = this,
//        fragmentManager = supportFragmentManager,
//        fragment = fragment,
//        behavior = behavior
//    )
//}
//
//fun Fragment.fragmentPagerAdapter(
//    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
//    fragment: () -> List<Fragment>
//): FragmentPagerAdapter = fragmentPagerAdapter(
//    context = requireContext(),
//    fragmentManager = childFragmentManager,
//    fragment = fragment,
//    behavior = behavior
//)
//
//fun Fragment.fragmentPagerAdapterLazy(
//    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
//    fragment: () -> List<Fragment>
//): Lazy<FragmentPagerAdapter> = lazy {
//    fragmentPagerAdapter(
//        context = requireContext(),
//        fragmentManager = childFragmentManager,
//        fragment = fragment,
//        behavior = behavior
//    )
//}
//
//fun fragmentPagerAdapter(
//    context: Context,
//    fragmentManager: FragmentManager,
//    fragment: () -> List<Fragment>,
//    behavior: Int = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
//): FragmentPagerAdapter = object : FragmentPagerAdapter(fragmentManager, behavior) {
//
//    private val fragments by lazy(fragment)
//
//    override fun getCount(
//    ): Int = fragments.size
//
//    override fun getItem(
//        position: Int
//    ): Fragment = fragments[position]
//
//    override fun getPageTitle(
//        position: Int
//    ): CharSequence? = (fragments[position] as? TitleHolder)?.title(context)
//
//}
//
////fun FragmentActivity.fragmentAdapter(
////    itemCount: () -> Int,
////    createFragment: (position: Int) -> Fragment
////) = fragmentAdapter(supportFragmentManager, lifecycle, itemCount, createFragment)
////
////fun FragmentActivity.fragmentAdapterLazy(
////    itemCount: () -> Int,
////    createFragment: (position: Int) -> Fragment
////) = lazy { fragmentAdapter(supportFragmentManager, lifecycle, itemCount, createFragment) }
////
////fun Fragment.fragmentAdapter(
////    itemCount: () -> Int,
////    createFragment: (position: Int) -> Fragment
////) = fragmentAdapter(childFragmentManager, lifecycle, itemCount, createFragment)
////
////fun Fragment.fragmentAdapterLazy(
////    itemCount: () -> Int,
////    createFragment: (position: Int) -> Fragment
////) = lazy { fragmentAdapter(childFragmentManager, lifecycle, itemCount, createFragment) }
////
////fun fragmentAdapter(
////    fragmentManager: FragmentManager,
////    lifecycle: Lifecycle,
////    itemCount: () -> Int,
////    createFragment: (position: Int) -> Fragment
////): FragmentStateAdapter = object : FragmentStateAdapter(fragmentManager, lifecycle) {
////
////    override fun createFragment(position: Int) = createFragment.invoke(position)
////
////    override fun getItemCount(): Int = itemCount.invoke()
////
////}
////
////fun ViewPager2.setCurrentItemField(position: Int) {
////    this::class.java.getDeclaredField("mCurrentItem").also {
////        it.isAccessible = true
////    }.set(this, position)
////}
