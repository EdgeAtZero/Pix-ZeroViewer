package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IllustrationRelatedData(
    @SerializedName("illusts")
    var Illustrations: List<Illustration> = listOf(),
    @SerializedName("next_url")
    var nextUrl: String = "" // https://app-api.pixiv.net/v2/illust/related?illust_id=76405553&filter=for_android&seed_illust_ids%5B0%5D=76405553&offset=30
) : Parcelable
