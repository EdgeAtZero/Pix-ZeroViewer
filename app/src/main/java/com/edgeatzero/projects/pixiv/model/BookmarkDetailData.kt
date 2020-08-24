package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookmarkDetailData(
    @SerializedName("bookmark_detail")
    var bookmarkDetail: BookmarkDetail = BookmarkDetail()
) : Parcelable
