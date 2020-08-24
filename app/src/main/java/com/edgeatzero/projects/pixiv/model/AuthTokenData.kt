package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthTokenData(
    @SerializedName("response")
    var response: Response = Response()
) : Parcelable {

    @Parcelize
    data class Response(
        @SerializedName("access_token")
        var accessToken: String = "",
        @SerializedName("device_token")
        var deviceToken: String = "",
        @SerializedName("expires_in")
        var expiresIn: Long = 0,
        @SerializedName("refresh_token")
        var refreshToken: String = "",
        @SerializedName("scope")
        var scope: String = "",
        @SerializedName("token_type")
        var tokenType: String = "",
        @SerializedName("user")
        var user: User = User()
    ) : Parcelable

    @Parcelize
    data class User(
        @SerializedName("account")
        var account: String = "",
        @SerializedName("id")
        var id: Long = 0,
        @SerializedName("is_mail_authorized")
        var isMailAuthorized: Boolean = false,
        @SerializedName("is_premium")
        var isPremium: Boolean = false,
        @SerializedName("mail_address")
        var mailAddress: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("profile_image_urls")
        var profileImageUrls: ProfileImageUrls = ProfileImageUrls(),
        @SerializedName("require_policy_agreement")
        var requirePolicyAgreement: Boolean = false,
        @SerializedName("x_restrict")
        var xRestrict: Int = 0
    ) : Parcelable

    @Parcelize
    data class ProfileImageUrls(
        @SerializedName("px_16x16")
        var px16x16: String = "",
        @SerializedName("px_170x170")
        var px170x170: String = "",
        @SerializedName("px_50x50")
        var px50x50: String = ""
    ) : Parcelable

}