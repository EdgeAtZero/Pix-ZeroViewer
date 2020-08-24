package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class UserIllustrationType(val value: String) : Parcelable {

    /**
     * 插图
     */
    ILLUSTRATION(UploadIllustrationParameter.UPLOAD_PARAMS_TYPE_ILLUSTRATION),

    /**
     * 漫画
     */
    MANGA(UploadIllustrationParameter.UPLOAD_PARAMS_TYPE_MANGA);

    override fun toString(): String = value

}
