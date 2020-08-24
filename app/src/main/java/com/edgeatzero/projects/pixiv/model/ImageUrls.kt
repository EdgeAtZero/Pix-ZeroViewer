package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageUrls(
    @SerializedName("large")
    var large: String = "", // https://i.pximg.net/c/600x1200_90/img-master/img/2020/03/20/01/19/28/80230341_p0_master1200.jpg
    @SerializedName("medium")
    var medium: String = "", // https://i.pximg.net/c/540x540_70/img-master/img/2020/03/20/01/19/28/80230341_p0_master1200.jpg
    @SerializedName("original")
    var original: String = "", // https://i.pximg.net/img-original/img/2020/03/20/01/19/28/80230341_p0.jpg
    @SerializedName("square_medium")
    var squareMedium: String? = null // https://i.pximg.net/c/360x360_70/img-master/img/2020/03/20/01/19/28/80230341_p0_square1200.jpg
) : Parcelable
