package com.edgeatzero.library.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

object EventBusUtils : Application.ActivityLifecycleCallbacks {

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity.eventBusRegistrable) EventBus.getDefault().register(activity)
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {

                    override fun onFragmentCreated(
                        fragmentManager: FragmentManager,
                        fragment: Fragment,
                        savedInstanceState: Bundle?
                    ) {
                        if (fragment.eventBusRegistrable) EventBus.getDefault().register(fragment)
                    }

                    override fun onFragmentDestroyed(
                        fragmentManager: FragmentManager,
                        fragment: Fragment
                    ) {
                        if (fragment.eventBusRegistrable)
                            EventBus.getDefault().unregister(fragment)
                    }

                }, true
            )
        }
    }

    override fun onActivityStarted(activity: Activity)  = Unit

    override fun onActivityPaused(activity: Activity)  = Unit

    override fun onActivityResumed(activity: Activity)  = Unit

    override fun onActivityStopped(activity: Activity)  = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle)  = Unit

    override fun onActivityDestroyed(activity: Activity) {
        if (activity.eventBusRegistrable) EventBus.getDefault().unregister(activity)
    }

    private val Any.eventBusRegistrable: Boolean
        get() = javaClass.declaredMethods.any { field -> field.annotations.any { annotation -> annotation is Subscribe } }

}
