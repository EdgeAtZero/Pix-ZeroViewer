package com.edgeatzero.projects.pixiv.http.service

import com.edgeatzero.library.common.ServiceCompanion
import com.edgeatzero.projects.pixiv.model.OneZeroDnsQueryData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface OneZeroApiService {

    companion object : ServiceCompanion("https://1.0.0.1/")

    @GET("dns-query")
    fun queryDns(
        @Header("accept") accept: String = "application/dns-json",
        @Query("name") name: String,
        @Query("type") type: String = "A",
        @Query("do") `do`: Boolean? = null,
        @Query("cd") cd: Boolean? = null
    ): Call<OneZeroDnsQueryData>

}
