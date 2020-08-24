package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrendTagData(
    @SerializedName("trend_tags")
    val trendTags: List<TrendTag> = listOf()
) : Parcelable
