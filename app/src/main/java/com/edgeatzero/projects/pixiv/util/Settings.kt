package com.edgeatzero.projects.pixiv.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edgeatzero.library.ext.getString
import com.edgeatzero.library.util.LocalUtils
import com.edgeatzero.library.util.Utils
import com.edgeatzero.projects.pixiv.http.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.util.*

object Settings : KodeinAware {

    override val kodein = Utils.kodein

    private val context by instance<Context>()

    private val accountRepository by instance<AccountRepository>()

    private val sharedPreferences by instance<SharedPreferences>("Settings")

    var authorization
        get() = sharedPreferences.getString(Key.AUTHORIZATION) ?: Default.AUTHORIZATION
        set(value) {
            sharedPreferences.edit().putString(Key.AUTHORIZATION, value).apply()
        }

    val isValidAccount: Boolean
        get() = accountId != Default.ACCOUNT_ID

    var account
        get() = accountRepository.queryAccount(accountId)
        set(value) {
            value?.let { accountRepository.insertAccount(it) }
        }

    var accountId
        get() = sharedPreferences.getLong(Key.ACCOUNT_ID, Default.ACCOUNT_ID)
        set(value) {
            sharedPreferences.edit().putLong(Key.ACCOUNT_ID, value).apply()
            accountIdMD.postValue(value)
        }

    private val accountIdMD by lazy { MutableLiveData<Long>() }
    val accountIdLD: LiveData<Long> by lazy { accountIdMD }

    var useProxy
        get() = sharedPreferences.getBoolean(Key.USE_PROXY, Default.USE_PROXY)
        set(value) {
            sharedPreferences.edit().putBoolean(Key.USE_PROXY, value).apply()
        }

    var locale
        get() = LocalUtils.parseLocale(sharedPreferences.getString(Key.LOCALE)) ?: Default.LOCALE
        set(value) {
            sharedPreferences.edit().putString(Key.LOCALE, value.toString()).apply()
        }

    var homeOptimization
        get() = sharedPreferences.getBoolean(Key.HOME_OPTIMIZATION, Default.HOME_OPTIMIZATION)
        set(value) {
            sharedPreferences.edit().putBoolean(Key.HOME_OPTIMIZATION, value).apply()
        }

    var likeUseOriginalTags
        get() =
            sharedPreferences.getBoolean(Key.LIKE_USE_ORIGINAL_TAGS, Default.LIKE_USE_ORIGINAL_TAGS)
        set(value) {
            sharedPreferences.edit().putBoolean(Key.LIKE_USE_ORIGINAL_TAGS, value).apply()
        }

    var nightMode
        get() = sharedPreferences.getInt(Key.NIGHT_MODE, Default.NIGHT_MODE)
        set(value) {
            sharedPreferences.edit().putInt(Key.NIGHT_MODE, value).apply()
        }

    init {
        // For Init LiveData's Data
        GlobalScope.launch(Dispatchers.Main) {
            accountIdMD.value = withContext(Dispatchers.IO) { accountId }
        }
    }

    object Key {

        const val ACCOUNT_ID = "account_id"

        const val AUTHORIZATION = "authorization"

        const val USE_PROXY = "use_proxy"

        const val LOCALE = "locale"

        const val HOME_OPTIMIZATION = "home_optimization"

        const val LIKE_USE_ORIGINAL_TAGS = "like_use_original_tags"

        const val NIGHT_MODE = "night_mode"

    }

    object Default {

        const val ACCOUNT_ID = -1L

        const val AUTHORIZATION = ""

        const val USE_PROXY = true

        @JvmStatic
        val LOCALE: Locale
            get() = getSystemLocale()

        const val HOME_OPTIMIZATION = true

        const val LIKE_USE_ORIGINAL_TAGS = false

        const val NIGHT_MODE = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

        private fun getSystemLocale() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }

    }

}
