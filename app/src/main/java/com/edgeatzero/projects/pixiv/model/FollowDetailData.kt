package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FollowDetailData(
    @SerializedName("follow_detail")
    var followDetail: FollowDetail = FollowDetail()
) : Parcelable
