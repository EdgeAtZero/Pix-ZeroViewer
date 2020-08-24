package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfilePublicity(
    @SerializedName("birth_day")
    var birthDay: String = "", // public
    @SerializedName("birth_year")
    var birthYear: String = "", // public
    @SerializedName("gender")
    var gender: String = "", // public
    @SerializedName("job")
    var job: String = "", // public
    @SerializedName("pawoo")
    var pawoo: Boolean = false, // true
    @SerializedName("region")
    var region: String = "" // public
) : Parcelable
