package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 内容类型
 */
@Parcelize
enum class ContentType(val value: String) : Parcelable {

    /**
     * 插图
     */
    ILLUSTRATION(UploadIllustrationParameter.UPLOAD_PARAMS_TYPE_ILLUSTRATION),

    /**
     * 漫画
     */
    MANGA(UploadIllustrationParameter.UPLOAD_PARAMS_TYPE_MANGA),

    /**
     * 小说
     */
    NOVEL("novel"),

    /**
     * 用户
     */
    USER("user");

    override fun toString(): String = value

}
