package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 过滤
 */
@Parcelize
enum class Filter(val value: String) : Parcelable {

    /**
     * android 平台
     */
    ANDROID("for_android"),

    /**
     * ios平台
     */
    IOS("for_ios");

    override fun toString(): String = value

}
