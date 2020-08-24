package com.edgeatzero.projects.pixiv.dagger

import com.edgeatzero.projects.pixiv.http.okhttp.*
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level as LogLevel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

private const val HTTP_CLIENT_MODULE_TAG = "httpClientModule"

val httpClientModule = Kodein.Module(HTTP_CLIENT_MODULE_TAG) {

    //Retrofit
    bind<Retrofit.Builder>() with provider { Retrofit.Builder() }

    bind<GsonConverterFactory>() with singleton { GsonConverterFactory.create(instance()) }

    //OkHttpClient
    bind<OkHttpClient.Builder>() with provider { OkHttpClient.Builder() }

    bind<StethoInterceptor>() with provider { StethoInterceptor() }

    bind<PixivAuthorizationInterceptor>() with singleton { PixivAuthorizationInterceptor(instance("Authorization")) }

    bind<PixivHeadersInterceptor>() with singleton { PixivHeadersInterceptor() }

    bind<PixivHostnameVerifier>() with singleton { PixivHostnameVerifier() }

    bind<PixivSSLSocketFactory>() with singleton { PixivSSLSocketFactory() }

    bind<PixivX509TrustManager>() with singleton { PixivX509TrustManager() }

    bind<PixivDns>() with singleton { PixivDns() }

    bind<OkHttpClient>("Common") with singleton {
        with(instance<OkHttpClient.Builder>()) {
            addInterceptor(instance<PixivHeadersInterceptor>())
            addInterceptor(instance<PixivAuthorizationInterceptor>())
            addInterceptor(httpLoggingInterceptor("Common"))
            addNetworkInterceptor(instance<StethoInterceptor>())
//            hostnameVerifier(instance())
//            sslSocketFactory(instance(), instance())
//            dns(instance<PixivDns>())
        }.build()
    }

    bind<OkHttpClient>("Authorization") with singleton {
        with(instance<OkHttpClient.Builder>()) {
            addInterceptor(instance<PixivHeadersInterceptor>())
            addInterceptor(httpLoggingInterceptor("Authorization"))
            addNetworkInterceptor(instance<StethoInterceptor>())
//            hostnameVerifier(instance())
//            sslSocketFactory(instance(), instance())
//            dns(instance<PixivDns>())
        }.build()
    }

    bind<OkHttpClient>("Glide") with singleton {
        with(instance<OkHttpClient.Builder>()) {
            addInterceptor(instance<PixivHeadersInterceptor>())
//            addInterceptor(httpLoggingInterceptor("Glide"))
            addNetworkInterceptor(instance<StethoInterceptor>())
        }.build()
    }

}

fun httpLoggingInterceptor(
    tag: String,
    level: LogLevel = LogLevel.BODY
): Interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {

    private val logger = Timber.tag(tag)

    override fun log(message: String) {
        logger.v(message)
    }

}).setLevel(level)
