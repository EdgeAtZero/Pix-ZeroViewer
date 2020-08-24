package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FailedData(
    @SerializedName("error")
    var error: String = "",
    @SerializedName("errors")
    var errors: Errors = Errors(),
    @SerializedName("has_error")
    var hasError: Boolean = false, // true
    @SerializedName("message")
    var message: String = "" // true
) : Parcelable {

    @Parcelize
    data class Errors(
        @SerializedName("system")
        var system: System = System(),
        @SerializedName("message")
        var message: String = "",
        @SerializedName("user_message")
        var userMessage: String = ""
    ) : Parcelable {

        @Parcelize
        data class System(
            @SerializedName("code")
            var code: Int = 0, // 1508
            @SerializedName("message")
            var message: String = "" // 103:pixiv ID、またはメールアドレス、パスワードが正しいかチェックしてください。
        ) : Parcelable

    }

    val errorMessage: String?
        get() {
            return listOf(
                errors.message,
                errors.userMessage,
                errors.system.message,
                message
            ).firstOrNull { it.isNotEmpty() }
        }

}
