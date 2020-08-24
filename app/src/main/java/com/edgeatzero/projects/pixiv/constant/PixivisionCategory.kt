package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class PixivisionCategory(val value: String) : Parcelable {

    ALL("all"),

    MANGA(UploadIllustrationParameter.UPLOAD_PARAMS_TYPE_MANGA);

    override fun toString(): String = value

}
