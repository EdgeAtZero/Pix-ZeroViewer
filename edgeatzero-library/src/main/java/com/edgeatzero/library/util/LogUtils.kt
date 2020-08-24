package com.edgeatzero.library.util

import com.edgeatzero.library.common.DebugTree
import com.edgeatzero.library.common.ReleaseTree
import timber.log.Timber

object LogUtils {

    fun init(isDebug: Boolean = true) {
        if (isDebug) Timber.plant(DebugTree()) else Timber.plant(ReleaseTree())
    }

    val allCalledMethodName: List<String>
        get() {
            val result = ArrayList<String>()
            Thread.getAllStackTraces()[Thread.currentThread()]?.map {
                it.methodName
            }?.filterIndexed { index, _ ->
                index > 3
            }?.let { result.addAll(it) }
            return result
        }

    val callerMethodName: String
        get() = Thread.getAllStackTraces()[Thread.currentThread()]!![5].methodName

    val myMethodName: String
        get() = Thread.getAllStackTraces()[Thread.currentThread()]!![4].methodName

    fun splitLogs(source: String, max: Int = 4000): List<String> {
        val result = ArrayList<String>()
        var i = 0
        val length: Int = source.length
        while (i < length) {
            var newline: Int = source.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = newline.coerceAtMost(i + max)
                result.add(source.substring(i, end))
                i = end
            } while (i < newline)
            i++
        }
        return result
    }

}
