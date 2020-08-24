@file:SuppressLint("LogNotTimber")

package com.edgeatzero.library.common

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber

open class DebugTree : Timber.DebugTree() {

//    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//        if (priority == Log.ASSERT) {
//            Log.wtf(tag, message)
//        } else {
//            Log.println(priority, tag, message)
//        }
//    }


}
