package com.edgeatzero.library.common

import android.util.Log

class ReleaseTree: DebugTree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return !(priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
    }

}
