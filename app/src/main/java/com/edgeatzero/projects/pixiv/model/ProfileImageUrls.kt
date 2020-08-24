package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileImageUrls(
    @SerializedName("medium")
    var medium: String = "" // https://i.pximg.net/user-profile/img/2020/03/18/20/56/30/18137907_9a31eff4160128b108fb63b3b85175c1_170.jpg
) : Parcelable
