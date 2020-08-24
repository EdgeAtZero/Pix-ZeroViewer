package com.edgeatzero.projects.pixiv.http.module

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.edgeatzero.projects.pixiv.ZeroViewerApplication
import okhttp3.OkHttpClient
import java.io.InputStream
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

@GlideModule
class OkHttpLibraryGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val okHttpClient by (context.applicationContext as ZeroViewerApplication).kodein.instance<OkHttpClient>("Glide")
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )
    }

}
