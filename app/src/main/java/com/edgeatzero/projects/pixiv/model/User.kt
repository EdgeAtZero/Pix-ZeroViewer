package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.edgeatzero.projects.pixiv.model.util.BindingUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("account")
    var account: String = "", // skyrick9413
    @SerializedName("id")
    override var id: Long = 0, // 33558705
    @SerializedName("is_followed")
    var originalIsFollowed: Boolean = false, // false
    @SerializedName("name")
    var name: String = "", // 画师JW
    @SerializedName("profile_image_urls")
    var profileImageUrls: ProfileImageUrls = ProfileImageUrls()
) : BaseObservable(), Parcelable, BindingUtils.IdHolder, BindingUtils.IsFollowedHolder {

    val headPicture: String
        get() = profileImageUrls.medium

    override var isFollowed: Boolean
        @Bindable
        get() = originalIsFollowed
        set(value) {
            if (originalIsFollowed == value) return
            originalIsFollowed = value
        }

}
