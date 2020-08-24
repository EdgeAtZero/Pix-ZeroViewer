package com.edgeatzero.projects.pixiv.http.service

import com.edgeatzero.library.common.ServiceCompanion
import com.edgeatzero.projects.pixiv.model.*
import retrofit2.Call
import retrofit2.http.*

interface PixivAccountApiService {

    companion object : ServiceCompanion("https://accounts.pixiv.net/")

    /**
     * 创建临时账户
     *
     * @param authorization 令牌
     * @param user_name 名称
     * @param ref 类型
     */
    @FormUrlEncoded
    @POST("api/provisional-accounts/create")
    fun provisionalAccountsCreate(
        @Header("Authorization") authorization: String = "l-f9qZ0ZyqSwRyZs8-MymbtWBbSxmCu1pmbOlyisou8",
        @Field("user_name") user_name: String,
        @Field("ref") ref: String = "pixiv_android_app_provisional_account"
    ): Call<AccountsCreateData>

    /**
     * 修改信息
     *
     * @param authorization 令牌
     * @param new_mail_address 邮箱地址
     * @param new_user_account Pixiv ID
     * @param current_password 当前密码
     * @param new_password 新密码
     */
    @FormUrlEncoded
    @POST("api/account/edit")
    fun accountEdit(
        @Header("Authorization") authorization: String,
        @Field("new_mail_address") new_mail_address: String? = null,
        @Field("new_user_account") new_user_account: String? = null,
        @Field("current_password") current_password: String? = null,
        @Field("new_password") new_password: String? = null
    ): Call<AccountsEditData>

}
