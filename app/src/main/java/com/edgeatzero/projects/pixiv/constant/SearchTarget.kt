package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 目标
 */
@Parcelize
enum class SearchTarget(private val value: String) : Parcelable {

//    /**
//     * 文本
//     */
//    TEXT("text"),
//
//    /**
//     * 关键字
//     */
//    KEYWORD("keyword"),

    /**
     *标题和内容匹配
     */
    TITLE_AND_CAPTION("title_and_caption"),

    /**
     * 标签完全匹配
     */
    EXACT_MATCH_FOR_TAGS("exact_match_for_tags"),

    /**
     * 标签部分匹配
     */
    PARTIAL_MATCH_FOR_TAGS("partial_match_for_tags");

    override fun toString(): String = value

}
