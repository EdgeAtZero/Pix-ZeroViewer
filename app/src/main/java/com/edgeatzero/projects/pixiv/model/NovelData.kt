package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NovelData(
    @SerializedName("novels")
    var novels: List<Novel> = listOf(),
    @SerializedName("next_url")
    var nextUrl: String? = null,
    @SerializedName("search_span_limit")
    var searchSpanLimit: Long = 0
) : Parcelable
