package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MetaSinglePage(
    @SerializedName("original_image_url")
    var originalImageUrl: String = "" // https://i.pximg.net/img-original/img/2020/03/30/01/10/32/80442598_p0.jpg
) : Parcelable
