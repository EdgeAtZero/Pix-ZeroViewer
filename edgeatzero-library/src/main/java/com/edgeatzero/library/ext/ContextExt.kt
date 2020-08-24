@file:Suppress("DEPRECATION")

package com.edgeatzero.library.ext

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.AnyThread
import androidx.annotation.ArrayRes
import androidx.annotation.MainThread
import androidx.core.content.getSystemService
import com.edgeatzero.library.util.Utils.context


val Context.isNightMode
    @MainThread
    get() = Configuration.UI_MODE_NIGHT_YES == (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)

fun Context.getStringArray(@ArrayRes res: Int?): Array<String> {
    return if (res != null) return resources.getStringArray(res) else emptyArray()
}

val Context.isPortrait: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

val Context.isLandscape: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

@AnyThread
fun Context.jumpBrowser(url: String) {
    val uri = Uri.parse(url)
    Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(this)
    }
}


@AnyThread
fun Context.isNetworkAvailable(
): Boolean = getSystemService<ConnectivityManager>()?.activeNetworkInfo?.isAvailable ?: false

@AnyThread
inline fun <reified T : Activity> Context.isTopActivity(): Boolean = isTopActivity(T::class.java)

@MainThread
fun Context.isTopActivity(clazz: Class<out Activity>): Boolean {
    val manager = getSystemService<ActivityManager>() ?: return false
    val name = manager.getRunningTasks(1)[0].topActivity ?: return false
    return name.className.contains(clazz.name)
}

@MainThread
fun Context.resolveAttribute(id: Int): TypedValue = TypedValue().also {
    theme.resolveAttribute(id, it, true)
}

@MainThread
inline fun <reified T : Activity> Context.startActivity(
    noinline initializer: (Intent.() -> Unit)? = null,
    noinline creator: (() -> Bundle)? = null
) {
    val intent = Intent(this, T::class.java)
    initializer?.invoke(intent)
    creator?.invoke()?.let { intent.putExtras(it) }
    startActivity(intent)
}

@MainThread
inline fun <reified T : Activity> Context.startActivity(
    options: Bundle? = null,
    noinline initializer: (Intent.() -> Unit)? = null,
    noinline creator: (() -> Bundle)? = null
) {
    val intent = Intent(this, T::class.java)
    initializer?.invoke(intent)
    creator?.invoke()?.let { intent.putExtras(it) }
    startActivity(intent, options)
}
