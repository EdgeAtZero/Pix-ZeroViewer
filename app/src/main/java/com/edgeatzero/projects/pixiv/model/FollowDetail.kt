package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.edgeatzero.projects.pixiv.constant.Restrict
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FollowDetail(
    @SerializedName("is_followed")
    var isFollowed: Boolean = false,
    @SerializedName("restrict")
    var originalRestrict: String = ""
) : Parcelable {

    val restrict: Restrict
        get() {
            return when (originalRestrict) {
                Restrict.PUBLIC.value -> Restrict.PUBLIC
                Restrict.PRIVATE.value -> Restrict.PRIVATE
                else -> Restrict.PUBLIC
            }
        }

}
