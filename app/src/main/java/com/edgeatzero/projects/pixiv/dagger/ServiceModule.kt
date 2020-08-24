package com.edgeatzero.projects.pixiv.dagger

import com.edgeatzero.projects.pixiv.http.service.PixivAccountApiService
import com.edgeatzero.projects.pixiv.http.service.PixivApplicationApiService
import com.edgeatzero.projects.pixiv.http.service.PixivAuthorizationApiService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val SERVICE_MODULE_TAG = "serviceModule"

val serviceModule = Kodein.Module(SERVICE_MODULE_TAG) {

    bind<PixivAccountApiService>() with singleton {
        with(instance<Retrofit.Builder>()) {
            baseUrl(PixivAccountApiService.baseUrl)
            client(instance("Authorization"))
            addConverterFactory(instance<GsonConverterFactory>())
            build()
        }.create<PixivAccountApiService>()
    }

    bind<PixivAuthorizationApiService>() with singleton {
        with(instance<Retrofit.Builder>()) {
            baseUrl(PixivAuthorizationApiService.baseUrl)
            client(instance("Authorization"))
            addConverterFactory(instance<GsonConverterFactory>())
            build()
        }.create<PixivAuthorizationApiService>()
    }

    bind<PixivApplicationApiService>() with singleton {
        with(instance<Retrofit.Builder>()) {
            baseUrl(PixivApplicationApiService.baseUrl)
            client(instance("Common"))
            addConverterFactory(instance<GsonConverterFactory>())
            build()
        }.create<PixivApplicationApiService>()
    }

}
