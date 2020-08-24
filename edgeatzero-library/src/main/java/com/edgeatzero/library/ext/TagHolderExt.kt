package com.edgeatzero.library.ext

import android.util.Log
import com.edgeatzero.library.common.TagHolder
import timber.log.Timber


fun TagHolder.logv(value: Any?): Unit = Timber.tag(TAG).log(Log.VERBOSE, value.toString())

fun TagHolder.logv(format: String, vararg args: Any): Unit = Timber.tag(TAG).log(Log.VERBOSE, format, args)

fun TagHolder.logd(value: Any?): Unit = Timber.tag(TAG).log(Log.DEBUG, value.toString())

fun TagHolder.logd(format: String, vararg args: Any): Unit = Timber.tag(TAG).log(Log.DEBUG, format, args)

fun TagHolder.logi(value: Any?): Unit = Timber.tag(TAG).log(Log.INFO, value.toString())

fun TagHolder.logi(format: String, vararg args: Any): Unit = Timber.tag(TAG).log(Log.INFO, format, args)

fun TagHolder.logw(value: Any?): Unit = Timber.tag(TAG).log(Log.WARN, value.toString())

fun TagHolder.logw(format: String, vararg args: Any): Unit = Timber.tag(TAG).log(Log.WARN, format, args)

fun TagHolder.loge(value: Any?): Unit = Timber.tag(TAG).log(Log.ERROR, value.toString())

fun TagHolder.loge(format: String, vararg args: Any): Unit = Timber.tag(TAG).log(Log.ERROR, format, args)

fun TagHolder.loga(value: Any?): Unit = Timber.tag(TAG).log(Log.ASSERT, value.toString())

fun TagHolder.loga(format: String, vararg args: Any): Unit = Timber.tag(TAG).log(Log.ASSERT, format, args)
