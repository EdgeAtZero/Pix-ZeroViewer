package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagData(
    @SerializedName("tags")
    var tags: List<Tag> = listOf()
) : Parcelable
