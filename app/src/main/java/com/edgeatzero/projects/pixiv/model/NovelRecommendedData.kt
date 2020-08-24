package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NovelRecommendedData(
    @SerializedName("next_url")
    var nextUrl: String? = null,
    @SerializedName("novels")
    var novels: List<Novel> = listOf(),
    @SerializedName("privacy_policy")
    var privacyPolicy: PrivacyPolicy = PrivacyPolicy(),
    @SerializedName("ranking_novels")
    var rankingNovels: List<Novel> = listOf()
) : Parcelable
