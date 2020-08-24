package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserPreviewData(
    @SerializedName("next_url")
    var nextUrl: String = "",
    @SerializedName("user_previews")
    var userPreviews: List<UserPreview> = listOf()
) : Parcelable
