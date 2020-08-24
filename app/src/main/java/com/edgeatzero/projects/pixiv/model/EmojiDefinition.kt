package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmojiDefinition(
    @SerializedName("id")
    var id: Long = 0,
    @SerializedName("image_url_medium")
    var imageUrlMedium: String = "",
    @SerializedName("slug")
    var slug: String = ""
) : Parcelable {

    val pattern
        get() = "\\(${slug}\\)".toPattern()

}
