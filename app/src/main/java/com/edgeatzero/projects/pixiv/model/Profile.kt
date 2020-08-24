package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    @SerializedName("address_id")
    var addressId: Long = 0, // 0
    @SerializedName("background_image_url")
    var backgroundImageUrl: String = "", // null
    @SerializedName("birth")
    var birth: String = "",
    @SerializedName("birth_day")
    var birthDay: String = "",
    @SerializedName("birth_year")
    var birthYear: Long = 0, // 0
    @SerializedName("country_code")
    var countryCode: String = "",
    @SerializedName("gender")
    var gender: String = "", // unknown
    @SerializedName("is_premium")
    var isPremium: Boolean = false, // false
    @SerializedName("is_using_custom_profile_image")
    var isUsingCustomProfileImage: Boolean = false, // true
    @SerializedName("job")
    var job: String = "",
    @SerializedName("job_id")
    var jobId: Long = 0, // 0
    @SerializedName("pawoo_url")
    var pawooUrl: String = "", // null
    @SerializedName("region")
    var region: String = "", // 日本
    @SerializedName("total_follow_users")
    var totalFollowUsers: Long = 0, // 1
    @SerializedName("total_illust_bookmarks_public")
    var totalIllustBookmarksPublic: Long = 0, // 7
    @SerializedName("total_illust_series")
    var totalIllustSeries: Long = 0, // 0
    @SerializedName("total_illusts")
    var totalIllusts: Long = 0, // 0
    @SerializedName("total_manga")
    var totalManga: Long = 0, // 0
    @SerializedName("total_mypixiv_users")
    var totalMypixivUsers: Long = 0, // 0
    @SerializedName("total_novel_series")
    var totalNovelSeries: Long = 0, // 0
    @SerializedName("total_novels")
    var totalNovels: Long = 0, // 0
    @SerializedName("twitter_account")
    var twitterAccount: String = "",
    @SerializedName("twitter_url")
    var twitterUrl: String = "", // null
    @SerializedName("webpage")
    var webpage: String = "" // null
) : Parcelable
