package com.edgeatzero.library.ext

import okhttp3.Call as OkHttpCall
import okhttp3.OkHttpClient
import okhttp3.internal.connection.RealCall
import retrofit2.Call as Retrofit2Call

fun Retrofit2Call<*>.tryGetClient(): OkHttpClient? {
    return try {
        val field0 = this::class.java.getDeclaredField("delegate")
        field0.isAccessible = true
        val call = field0.get(this)
        val field1 = call::class.java.getDeclaredMethod("getRawCall")
        field1.isAccessible = true
        (field1.invoke(call) as OkHttpCall).client
    } catch (e: Exception) {
        null
    }
}

val OkHttpCall.client
    get() = (this as RealCall).client
