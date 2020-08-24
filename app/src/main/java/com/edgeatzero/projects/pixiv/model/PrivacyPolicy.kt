package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PrivacyPolicy(
    @SerializedName("message")
    var message: String = "", // pixiv响应EU的通用数据保护条例，修订了隐私政策。
    @SerializedName("url")
    var url: String = "", // https://www.pixiv.net/terms/?page=privacy&appname=pixiv_ios
    @SerializedName("version")
    var version: String = "" // 1-zh
) : Parcelable
