package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IllustrationRecommendedData(
    @SerializedName("contest_exists")
    var contestExists: Boolean = false, // false
    @SerializedName("illusts")
    var Illustrations: List<Illustration> = listOf(),
    @SerializedName("next_url")
    var nextUrl: String? = null, // https://app-api.pixiv.net/v1/illust/recommended?filter=for_android&include_ranking_illusts=false&include_privacy_policy=false&min_bookmark_id_for_recent_illust=0&max_bookmark_id_for_recommend=-1&offset=30
    @SerializedName("privacy_policy")
    var privacyPolicy: PrivacyPolicy = PrivacyPolicy(),
    @SerializedName("ranking_illusts")
    var rankingIllustrations: List<Illustration> = listOf()
) : Parcelable
