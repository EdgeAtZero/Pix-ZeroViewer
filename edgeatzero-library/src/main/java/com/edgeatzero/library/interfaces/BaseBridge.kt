package com.edgeatzero.library.interfaces

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

interface BaseBridge {

    fun context(): Context

    fun lifecycleOwner(): LifecycleOwner? = null

    fun fragmentManager(): FragmentManager? = null

}
