package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 工具
 */
@Parcelize
enum class SearchIllustTool(val value: String) : Parcelable {

    PHOTOSHOP("photoshop"),

    ILLUSTRATOR("illustrator"),

    FIREWORKS("fireworks");

    override fun toString(): String = value

}
