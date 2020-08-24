package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.edgeatzero.projects.pixiv.constant.Restrict
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookmarkDetail(
    @SerializedName("is_bookmarked")
    var isBookmarked: Boolean = false,
    @SerializedName("restrict")
    var originalRestrict: String = "",
    @SerializedName("tags")
    var tags: List<Tag> = listOf()
) : Parcelable {

    @Parcelize
    data class Tag(
        @SerializedName("is_registered")
        var isRegistered: Boolean = false,
        @SerializedName("name")
        var name: String = ""
    ) : Parcelable

    val restrict: Restrict
        get() {
            return when (originalRestrict) {
                Restrict.PUBLIC.value -> Restrict.PUBLIC
                Restrict.PRIVATE.value -> Restrict.PRIVATE
                else -> Restrict.PUBLIC
            }
        }


}