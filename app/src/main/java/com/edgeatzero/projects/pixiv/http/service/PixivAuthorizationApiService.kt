package com.edgeatzero.projects.pixiv.http.service

import com.edgeatzero.library.common.ServiceCompanion
import com.edgeatzero.projects.pixiv.constant.ClientConstant.CLIENT_ID
import com.edgeatzero.projects.pixiv.constant.ClientConstant.CLIENT_SECRET
import com.edgeatzero.projects.pixiv.model.*
import retrofit2.Call
import retrofit2.http.*

interface PixivAuthorizationApiService {

    companion object : ServiceCompanion("https://oauth.secure.pixiv.net/")

    /**
     * 获取 token
     *
     * @param client_id id
     * @param client_secret secret
     * @param grant_type 类型
     * @param username Pixiv ID / 邮箱
     * @param password 密码
     * @param device_token 设备 token
     * @param get_secure_url 获取安全网址
     * @param include_policy 同意政策
     */
    @FormUrlEncoded
    @POST("auth/token")
    fun getAuthToken(
        @Field("client_id") client_id: String = CLIENT_ID,
        @Field("client_secret") client_secret: String = CLIENT_SECRET,
        @Field("grant_type") grant_type: String = "password",
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("device_token") device_token: String = "pixiv",
        @Field("get_secure_url") get_secure_url: Boolean = true,
        @Field("include_policy") include_policy: Boolean = true
    ): Call<AuthTokenData>

    /**
     * 刷新 token
     *
     * @param client_id id
     * @param client_secret secret
     * @param grant_type 类型
     * @param refresh_token 刷新 token
     * @param device_token 设备 token
     * @param get_secure_url 获取安全网址
     * @param include_policy 同意政策
     */
    @FormUrlEncoded
    @POST("auth/token")
    fun refreshAuthToken(
        @Field("client_id") client_id: String = CLIENT_ID,
        @Field("client_secret") client_secret: String = CLIENT_SECRET,
        @Field("grant_type") grant_type: String = "refresh_token",
        @Field("refresh_token") refresh_token: String,
        @Field("device_token") device_token: String,
        @Field("get_secure_url") get_secure_url: Boolean = true,
        @Field("include_policy") include_policy: Boolean = true
    ): Call<AuthTokenData>

}
