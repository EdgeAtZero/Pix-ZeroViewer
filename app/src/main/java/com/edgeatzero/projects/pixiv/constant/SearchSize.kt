package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 尺寸
 */
@Parcelize
enum class SearchSize(val value: String) : Parcelable {

    MINIMUM("minimum"),

    MEDIUM("medium"),

    LARGE("large");

    override fun toString(): String = value

}
