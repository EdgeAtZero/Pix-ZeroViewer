package com.edgeatzero.library.ext

import android.util.Log
import com.edgeatzero.library.common.TagHolder
import timber.log.Timber

inline fun logv(value: Any?): Unit = Timber.log(Log.VERBOSE, value.toString())

inline fun logv(format: String, vararg args: Any): Unit = Timber.log(Log.VERBOSE, format, args)

inline fun logd(value: Any?): Unit = Timber.log(Log.DEBUG, value.toString())

inline fun logd(format: String, vararg args: Any): Unit = Timber.log(Log.DEBUG, format, args)

inline fun logi(value: Any?): Unit = Timber.log(Log.INFO, value.toString())

inline fun logi(format: String, vararg args: Any): Unit = Timber.log(Log.INFO, format, args)

inline fun logw(value: Any?): Unit = Timber.log(Log.WARN, value.toString())

inline fun logw(format: String, vararg args: Any): Unit = Timber.log(Log.WARN, format, args)

inline fun loge(value: Any?): Unit = Timber.log(Log.ERROR, value.toString())

inline fun loge(format: String, vararg args: Any): Unit = Timber.log(Log.ERROR, format, args)

inline fun loga(value: Any?): Unit = Timber.log(Log.ASSERT, value.toString())

inline fun loga(format: String, vararg args: Any): Unit = Timber.log(Log.ASSERT, format, args)
