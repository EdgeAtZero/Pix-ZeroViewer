package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 宽比高
 */
@Parcelize
enum class SearchAspectRatio(val value: String) : Parcelable {

    PORTRAIT("portrait"),

    LANDSCAPE("landscape"),

    SQUARE("square");

    override fun toString(): String = value

}
