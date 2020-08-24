package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class Restrict(val value: String) : Parcelable {

    /**
     * 公开
     */
    PUBLIC(UploadIllustrationParameter.UPLOAD_PARAMS_PUBLICITY_PUBLIC),

    /**
     * 私人
     */
    PRIVATE(UploadIllustrationParameter.UPLOAD_PARAMS_PUBLICITY_PRIVATE),

    /**
     * 全部
     */
    ALL("all");

    override fun toString(): String = value

}
