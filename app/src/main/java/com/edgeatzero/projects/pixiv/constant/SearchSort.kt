package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 * 排序
 */
@Keep
@Parcelize
enum class SearchSort(private val value: String) : Parcelable {

    /**
     * 由新到旧
     */
    DATE_DESC("date_desc"),

    /**
     * 由旧到新
     */
    DATE_ASC("date_asc"),

    /**
     * 热门排序
     */
    POPULAR_DESC("popular_desc"),

    /**
     * 热门男性排行
     */
    POPULAR_MALE_DESC("popular_male_desc"),

    /**
     * 热门女性排行
     */
    POPULAR_FEMALE_DESC("popular_female_desc");

    override fun toString(): String = value

}
