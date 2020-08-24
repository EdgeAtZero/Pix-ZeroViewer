package com.edgeatzero.projects.pixiv

import android.app.Application
import com.edgeatzero.projects.pixiv.dagger.*
import com.edgeatzero.library.util.EventBusUtils
import com.edgeatzero.library.util.LogUtils
import com.edgeatzero.library.util.Utils
import com.edgeatzero.projects.pixiv.model.util.EmojiUtils
import com.facebook.stetho.Stetho
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.android.x.androidXModule

class ZeroViewerApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidModule(this@ZeroViewerApplication))
        import(androidXModule(this@ZeroViewerApplication))
        import(databaseModule)
        import(httpClientModule)
        import(repositoryModule)
        import(serializableModule)
        import(serviceModule)
        import(sharedPreferencesModule)
    }

    override fun onCreate() {
        super.onCreate()
        Utils.initContext(this)
        Utils.initKodein(kodein)
        LogUtils.init(BuildConfig.DEBUG)
        EventBusUtils.init(this)
        initStetho()
        GlobalScope.launch(Dispatchers.IO) { EmojiUtils.init() }
    }

    private fun initStetho() {
        with(Stetho.newInitializerBuilder(this)) {
            enableDumpapp(Stetho.defaultDumperPluginsProvider(this@ZeroViewerApplication))
            enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this@ZeroViewerApplication))
            build()
        }.also { Stetho.initialize(it) }
    }

}
