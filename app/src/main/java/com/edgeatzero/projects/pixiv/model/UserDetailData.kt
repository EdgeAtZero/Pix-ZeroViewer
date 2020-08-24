package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetailData(
    @SerializedName("profile")
    var profile: Profile = Profile(),
    @SerializedName("profile_publicity")
    var profilePublicity: ProfilePublicity = ProfilePublicity(),
    @SerializedName("user")
    var user: User = User(),
    @SerializedName("workspace")
    var workspace: Workspace = Workspace()
) : Parcelable {

    @Parcelize
    data class User(
        @SerializedName("account")
        var account: String = "", // user_ufca5857
        @SerializedName("comment")
        var comment: String = "",
        @SerializedName("id")
        var id: Long = 0, // 51266706
        @SerializedName("is_followed")
        var isFollowed: Boolean = false, // false
        @SerializedName("name")
        var name: String = "", // LP
        @SerializedName("profile_image_urls")
        var profileImageUrls: ProfileImageUrls = ProfileImageUrls()
    ) : Parcelable

}
