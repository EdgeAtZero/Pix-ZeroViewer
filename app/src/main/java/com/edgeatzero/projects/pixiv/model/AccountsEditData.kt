package com.edgeatzero.projects.pixiv.model

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class AccountsEditData(
    @SerializedName("body")
    var body: Body = Body(),
    @SerializedName("error")
    var error: Boolean = false,
    @SerializedName("message")
    var message: String = ""
) : Parcelable {

    @Parcelize
    data class Body(
        @SerializedName("is_succeed")
        var isSucceed: Boolean = false,
        @SerializedName("validation_errors")
        var validationErrors: ValidationErrors = ValidationErrors()
    ) : Parcelable {

        @Parcelize
        data class ValidationErrors(
            @SerializedName("old_password")
            var oldPassword: String = "",
            @SerializedName("password")
            var password: String = ""
        ) : Parcelable

    }

}
