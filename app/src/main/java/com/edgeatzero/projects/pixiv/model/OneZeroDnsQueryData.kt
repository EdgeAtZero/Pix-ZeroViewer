package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OneZeroDnsQueryData(
    @SerializedName("AD")
    var aD: Boolean = false, // false
    @SerializedName("Answer")
    var answer: List<Answer> = listOf(),
    @SerializedName("CD")
    var cD: Boolean = false, // false
    @SerializedName("Question")
    var question: List<Question> = listOf(),
    @SerializedName("RA")
    var rA: Boolean = false, // true
    @SerializedName("RD")
    var rD: Boolean = false, // true
    @SerializedName("Status")
    var status: Int = 0, // 0
    @SerializedName("TC")
    var tC: Boolean = false // false
) : Parcelable {

    @Parcelize
    data class Answer(
        @SerializedName("data")
        var `data`: String = "", // 210.140.131.225
        @SerializedName("name")
        var name: String = "", // app-api.pixiv.net.
        @SerializedName("TTL")
        var tTL: Int = 0, // 81
        @SerializedName("type")
        var type: Int = 0 // 1
    ) : Parcelable

    @Parcelize
    data class Question(
        @SerializedName("name")
        var name: String = "", // app-api.pixiv.net.
        @SerializedName("type")
        var type: Int = 0 // 1
    ) : Parcelable

}
