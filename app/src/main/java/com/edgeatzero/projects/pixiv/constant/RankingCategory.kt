package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import androidx.annotation.StringRes
import com.edgeatzero.projects.pixiv.R.string.*
import kotlinx.android.parcel.Parcelize

/**
 * 排行
 */
@Parcelize
enum class RankingCategory(
    val contentType: ContentType,
    @StringRes val res: Int,
    val string: String,
    val type: Type
) : Parcelable {

    ILLUSTRATION_DAILY(ContentType.ILLUSTRATION, constant_ranking_daily, "day", Type.GENERAL),

    ILLUSTRATION_WEEKLY(ContentType.ILLUSTRATION, constant_ranking_weekly, "week", Type.GENERAL),

    ILLUSTRATION_MONTHLY(ContentType.ILLUSTRATION, constant_ranking_monthly, "month", Type.GENERAL),

    ILLUSTRATION_ROOKIE(
        ContentType.ILLUSTRATION,
        constant_ranking_rookie,
        "week_rookie",
        Type.GENERAL
    ),

    ILLUSTRATION_ORIGINAL(
        ContentType.ILLUSTRATION,
        constant_ranking_original,
        "week_original",
        Type.GENERAL
    ),

    ILLUSTRATION_MALE(ContentType.ILLUSTRATION, constant_ranking_male, "day_male", Type.GENERAL),

    ILLUSTRATION_FEMALE(
        ContentType.ILLUSTRATION,
        constant_ranking_female,
        "day_female",
        Type.GENERAL
    ),

    ILLUSTRATION_DAILY_R18(
        ContentType.ILLUSTRATION,
        constant_ranking_daily_r18,
        "day_r18",
        Type.R18
    ),

    ILLUSTRATION_WEEKLY_R18(
        ContentType.ILLUSTRATION,
        constant_ranking_weekly_r18,
        "week_r18",
        Type.R18
    ),

    ILLUSTRATION_MALE_R18(
        ContentType.ILLUSTRATION,
        constant_ranking_male_r18,
        "day_male_r18",
        Type.R18
    ),

    ILLUSTRATION_FEMALE_R18(
        ContentType.ILLUSTRATION,
        constant_ranking_female_r18,
        "day_female_r18",
        Type.R18
    ),

    ILLUSTRATION_R18G(ContentType.ILLUSTRATION, constant_ranking_r18g, "week_r18g", Type.R18G),


    MANGA_DAILY(ContentType.MANGA, constant_ranking_daily, "day_manga", Type.GENERAL),

    MANGA_WEEKLY(ContentType.MANGA, constant_ranking_weekly, "week_manga", Type.GENERAL),

    MANGA_MONTHLY(ContentType.MANGA, constant_ranking_monthly, "month_manga", Type.GENERAL),

    MANGA_ROOKIE(ContentType.MANGA, constant_ranking_rookie, "week_rookie_manga", Type.GENERAL),

    MANGA_DAILY_R18(ContentType.MANGA, constant_ranking_daily_r18, "day_r18_manga", Type.R18),

    MANGA_WEEKLY_R18(ContentType.MANGA, constant_ranking_weekly_r18, "week_r18_manga", Type.R18),

    MANGA_WEEKLY_R18G(ContentType.MANGA, constant_ranking_weekly_r18, "week_r18g_manga", Type.R18G),


    NOVEL_DAILY(ContentType.NOVEL, constant_ranking_daily, "day", Type.GENERAL),

    NOVEL_WEEKLY(ContentType.NOVEL, constant_ranking_weekly, "week", Type.GENERAL),

    NOVEL_MALE(ContentType.NOVEL, constant_ranking_male, "day_male", Type.GENERAL),

    NOVEL_FEMALE(ContentType.NOVEL, constant_ranking_female, "day_female", Type.GENERAL),

    NOVEL_ROOKIE(ContentType.NOVEL, constant_ranking_rookie, "week_rookie", Type.GENERAL),

    NOVEL_DAILY_R18(ContentType.NOVEL, constant_ranking_daily_r18, "day_r18", Type.R18),

    NOVEL_WEEKLY_R18(ContentType.NOVEL, constant_ranking_weekly_r18, "week_r18", Type.R18);

    override fun toString() = string

    companion object {

        @JvmStatic
        val illustration by lazy {
            arrayOf(
                ILLUSTRATION_DAILY,
                ILLUSTRATION_WEEKLY,
                ILLUSTRATION_MONTHLY,
                ILLUSTRATION_ROOKIE,
                ILLUSTRATION_ORIGINAL,
                ILLUSTRATION_MALE,
                ILLUSTRATION_FEMALE,
                ILLUSTRATION_DAILY_R18,
                ILLUSTRATION_WEEKLY_R18,
                ILLUSTRATION_MALE_R18,
                ILLUSTRATION_FEMALE_R18,
                ILLUSTRATION_R18G
            )
        }

        @JvmStatic
        val manga by lazy {
            arrayOf(
                MANGA_DAILY,
                MANGA_WEEKLY,
                MANGA_MONTHLY,
                MANGA_ROOKIE,
                MANGA_DAILY_R18,
                MANGA_WEEKLY_R18,
                MANGA_WEEKLY_R18G
            )
        }

        @JvmStatic
        val novel by lazy {
            arrayOf(
                NOVEL_DAILY,
                NOVEL_WEEKLY,
                NOVEL_MALE,
                NOVEL_FEMALE,
                NOVEL_ROOKIE,
                NOVEL_DAILY_R18,
                NOVEL_WEEKLY_R18
            )
        }

    }

    /**
     * 类型
     */

    @Parcelize
    enum class Type : Parcelable {
        /**
         * 普通
         */
        GENERAL,

        /**
         * R18
         */
        R18,

        /**
         * R18G
         */
        R18G;

    }

}
