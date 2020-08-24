package com.edgeatzero.projects.pixiv.dagger

import android.content.Context
import android.content.SharedPreferences
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


private const val SHARED_PREFERENCES_MODULE_TAG = "sharedPreferencesModule"

val sharedPreferencesModule = Kodein.Module(SHARED_PREFERENCES_MODULE_TAG) {

    bind<SharedPreferences>("Settings") with singleton {
        instance<Context>().getSharedPreferences("Settings", Context.MODE_PRIVATE)
    }

}
