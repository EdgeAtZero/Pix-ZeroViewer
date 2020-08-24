package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountsCreateData(
    @SerializedName("body")
    var body: Body = Body(),
    @SerializedName("error")
    var error: Boolean = false,
	@SerializedName("message")
    var message: String = ""
) : Parcelable {

    @Parcelize
    data class Body(
        @SerializedName("device_token")
        var deviceToken: String = "",
	@SerializedName("password")
        var password: String = "",
	@SerializedName("user_account")
        var userAccount: String = ""
	) : Parcelable

}