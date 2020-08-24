package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentaryData(
    @SerializedName("comments")
    var comments: List<Commentary> = listOf(),
    @SerializedName("next_url")
    var nextUrl: String? = null
) : Parcelable
