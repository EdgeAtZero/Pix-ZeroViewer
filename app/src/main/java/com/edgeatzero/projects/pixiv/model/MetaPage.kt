package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MetaPage(
    @SerializedName("image_urls")
    var imageUrls: ImageUrls = ImageUrls()
) : Parcelable
