package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Series(
    @SerializedName("title")
    var title: String = "",
    @SerializedName("id")
    var id: Long = 0
) : Parcelable
