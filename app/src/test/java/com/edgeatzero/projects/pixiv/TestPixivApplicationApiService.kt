package com.edgeatzero.projects.pixiv

import com.edgeatzero.library.common.ServiceCompanion
import com.edgeatzero.projects.pixiv.constant.Filter
import com.edgeatzero.projects.pixiv.model.*
import com.edgeatzero.projects.pixiv.util.Settings
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface TestPixivApplicationApiService {

    companion object : ServiceCompanion("https://app-api.pixiv.net/")

    /**
     * 应用信息
     */
    @GET("/v1/application-info/android")
    fun applicationInfoAndroid(
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/edit/novel/draft")
    fun editNovelDraft(
        @Header("Authorization") authorization: String,
        @Field("draft_id") draft_id: Long,
        @Field("title") title: String,
        @Field("caption") caption: String,
        @Field("cover_id") cover_id: Int,
        @Field("text") text: String,
        @Field("restrict") restrict: String,
        @Field("x_restrict") x_restrict: String,
        @Field("tags[]") tags: List<String>,
        @Field("is_original") is_original: Int
    ): Call<ResponseBody>

    @GET("v1/emoji")
    fun emoji(
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/feedback")
    fun feedback(
        @Header("Authorization") authorization: String,
        @Field("message") message: String,
        @Field("device") device: String,
        @Field("dimension01") dimension01: String,
        @Field("dimension02") dimension02: String,
        @Field("dimension03") dimension03: String,
        @Field("dimension04") dimension04: String
    ): Call<ResponseBody>

    @GET("/idp-urls")
    fun idpUrls(): Call<ResponseBody>

    /**
     * 收藏插图
     *
     * @param authorization 令牌
     * @param illust_id id
     * @param restrict 类型
     * @param tags 标签
     */
    @FormUrlEncoded
    @POST("/v2/illust/bookmark/add")
    fun illustrationBookmarkAdd(
        @Header("Authorization") authorization: String,
        @Field("illust_id") illust_id: Long,
        @Field("restrict") restrict: String,
        @Field("tags[]") tags: List<String>? = null
    ): Call<ResponseBody>

    /**
     * 取消收藏插图
     *
     * @param authorization 令牌
     * @param illust_id id
     */
    @FormUrlEncoded
    @POST("/v1/illust/bookmark/delete")
    fun illustrationBookmarkDelete(
        @Header("Authorization") authorization: String,
        @Field("illust_id") illust_id: Long
    ): Call<ResponseBody>

    /**
     * 插图 tag 信息
     *
     * @param authorization 令牌
     * @param illust_id id
     */
    @GET("/v2/illust/bookmark/detail")
    fun illustrationBookmarkDetail(
        @Header("Authorization") authorization: String,
        @Query("illust_id") illust_id: Long
    ): Call<ResponseBody>

    @GET("/v1/illust/bookmark/users")
    fun illustrationBookmarkUsers(
        @Header("Authorization") authorization: String,
        @Query("illust_id") illust_id: Long,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    /**
     * 插图添加评论
     *
     * @param authorization 令牌
     * @param illust_id 插图 id
     * @param comment 评论内容
     * @param parent_comment_id 回复的评论 id
     */
    @FormUrlEncoded
    @POST("/v1/illust/comment/add")
    fun illustrationCommentAdd(
        @Header("Authorization") authorization: String,
        @Field("illust_id") illust_id: Long,
        @Field("comment") comment: String,
        @Field("parent_comment_id") parent_comment_id: Long?
    ): Call<ResponseBody>

    /**
     * 插图评论删除
     *
     * @param authorization 令牌
     * @param comment_id id
     */
    @FormUrlEncoded
    @POST("/v1/illust/comment/delete")
    fun illustrationCommentDelete(
        @Header("Authorization") authorization: String,
        @Field("comment_id") comment_id: Long
    ): Call<ResponseBody>

    /**
     * 插图评论所有回复
     *
     * @param authorization 令牌
     * @param comment_id id
     */
    @GET("/v1/illust/comment/replies")
    fun illustrationCommentReplies(
        @Header("Authorization") authorization: String,
        @Query("comment_id") comment_id: Long
    ): Call<ResponseBody>

    /**
     * 插图全部评论
     *
     * @param authorization 令牌
     * @param illust_id id
     */
    @GET("/v2/illust/comments")
    fun illustrationComments(
        @Header("Authorization") authorization: String,
        @Query("illust_id") illust_id: Long
    ): Call<ResponseBody>


    /**
     * 插画删除
     *
     * @param authorization 令牌
     * @param illust_id id
     */
    @FormUrlEncoded
    @POST("/v1/illust/delete")
    fun illustrationDelete(
        @Header("Authorization") authorization: String,
        @Field("illust_id") illust_id: Long
    ): Call<ResponseBody>

    /**
     * 插图详情
     *
     * @param authorization 令牌
     * @param illust_id id
     * @param filter 过滤
     */
    @GET("/v1/illust/detail")
    fun illustrationDetail(
        @Header("Authorization") authorization: String,
        @Query("illust_id") illust_id: Long,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    /**
     * 获得关注用户的动态
     *
     * @param authorization 令牌
     * @param restrict 类型
     */
    @GET("/v2/illust/follow")
    fun illustrationFollow(
        @Header("Authorization") authorization: String,
        @Query("restrict") restrict: String
    ): Call<ResponseBody>

    /**
     * 获得好友用户的动态
     *
     * @param authorization 令牌
     */
    @GET("/v2/illust/mypixiv")
    fun illustrationMyPixiv(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @GET("/v1/illust/new")
    fun illustrationNew(
        @Header("Authorization") authorization: String,
        @Query("content_type") content_type: String,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    /**
     * 排行信息
     *
     * @param authorization 令牌
     * @param mode 模式
     * @param date 日期 yyyy-MM-dd
     */
    @GET("/v1/illust/ranking")
    fun illustrationRanking(
        @Header("Authorization") authorization: String,
        @Query("mode") mode: String,
        @Query("date") date: String? = null,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    /**
     * 插图推荐
     *
     * @param authorization 令牌
     * @param bookmark_illust_ids ？？？
     * @param filter 过滤
     * @param include_ranking_illusts 是否包含排行榜的部分信息
     * @param include_privacy_policy 隐私政策啥的
     */
    @GET("/v1/illust/recommended")
    fun illustrationRecommended(
        @Header("Authorization") authorization: String,
        @Query("bookmark_illust_ids") bookmark_illust_ids: String? = null,
        @Query("filter") filter: String = Filter.ANDROID.toString(),
        @Query("include_ranking_illusts") include_ranking_illusts: Boolean = false,
        @Query("include_privacy_policy") include_privacy_policy: Boolean = false
    ): Call<ResponseBody>

    /**
     * 插图的相关推荐
     */
    @GET("/v2/illust/related")
    fun illustrationRelated(
        @Header("Authorization") authorization: String,
        @Query("illust_id") illust_id: Long,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @GET("/v1/illust/series")
    fun illustrationSeries(
        @Header("Authorization") authorization: String,
        @Query("illust_series_id") illust_series_id: Long,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @GET("/v1/illust-series/illust")
    fun illustrationSeriesIllustration(
        @Header("Authorization") authorization: String,
        @Query("illust_id") illust_id: Long,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @GET("v1/live/list")
    fun liveList(
        @Header("Authorization") authorization: String,
        @Query("list_type") list_type: String
    ): Call<ResponseBody>

    @POST("v1/mail-authentication/send")
    fun mailAuthenticationSend(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    /**
     * 漫画推荐
     *
     * @param authorization 令牌
     * @param bookmark_illust_ids ？？？
     * @param filter 过滤
     * @param include_ranking_illusts 是否包含排行榜的部分信息
     * @param include_privacy_policy 隐私政策啥的
     */
    @GET("/v1/manga/recommended")
    fun mangaRecommended(
        @Header("Authorization") authorization: String,
        @Query("bookmark_illust_ids") bookmark_illust_ids: String? = null,
        @Query("filter") filter: String = Filter.ANDROID.toString(),
        @Query("include_ranking_illusts") include_ranking_illusts: Boolean = false,
        @Query("include_privacy_policy") include_privacy_policy: Boolean = false
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/mute/edit")
    fun muteEdit(
        @Header("Authorization") authorization: String,
        @Field("add_user_ids[]") add_user_ids: List<Long>,
        @Field("delete_user_ids[]") delete_user_ids: List<Long>,
        @Field("add_tags[]") add_tags: List<String>,
        @Field("delete_tags[]") delete_tags: List<String>
    ): Call<ResponseBody>

    @GET("/v1/mute/list")
    fun muteList(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @GET("/v1/notification/new-from-following")
    fun notificationNewFromFollowing(
        @Header("Authorization") authorization: String,
        @Query("latest_seen_illust_id") latest_seen_illust_id: Long? = null,
        @Query("latest_seen_novel_id") latest_seen_novel_id: Long? = null,
        @Query("last_notified_datetime") last_notified_datetime: String
    ): Call<ResponseBody>

    @GET("/v1/notification/settings")
    fun notificationSettings(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/notification/settings/edit")
    fun notificationSettingsEdit(
        @Header("Authorization") authorization: String,
        @Field("enabled_notification_types[]") enabled_notification_types: List<Int>
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("v1/notification/user/register")
    fun notificationUserRegister(
        @Header("Authorization") authorization: String,
        @Field("timezone_offset") timezone_offset: Int,
        @Field("disable_notifications") disable_notifications: Boolean
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v2/novel/bookmark/add")
    fun novelBookmarkAdd(
        @Header("Authorization") authorization: String,
        @Field("novel_id") novel_id: Long,
        @Field("restrict") restrict: String,
        @Field("tags[]") tags: List<String>? = null
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/novel/bookmark/delete")
    fun novelBookmarkDelete(
        @Header("Authorization") authorization: String,
        @Field("novel_id") novel_id: Long
    ): Call<ResponseBody>

    @GET("/v2/novel/bookmark/detail")
    fun novelBookmarkDetail(
        @Header("Authorization") authorization: String,
        @Query("novel_id") novel_id: Long
    ): Call<ResponseBody>

    @GET("/v1/novel/bookmark/users")
    fun novelBookmarkUsers(
        @Header("Authorization") authorization: String,
        @Query("novel_id") novel_id: Long
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("v1/novel/comment/add")
    fun novelCommentAdd(
        @Header("Authorization") str: String,
        @Field("novel_id") novel_id: Long,
        @Field("comment") comment: String,
        @Field("parent_comment_id") parent_comment_id: Long? = null
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/novel/comment/delete")
    fun novelCommentDelete(
        @Header("Authorization") authorization: String,
        @Field("comment_id") comment_id: Long
    ): Call<ResponseBody>

    @GET("/v1/novel/comment/replies")
    fun novelCommentReplies(
        @Header("Authorization") authorization: String,
        @Query("comment_id") comment_id: Long
    ): Call<ResponseBody>

    @GET("/v2/novel/comments")
    fun novelComments(
        @Header("Authorization") authorization: String,
        @Query("novel_id") novel_id: Long
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/novel/delete")
    fun novelDelete(
        @Header("Authorization") authorization: String,
        @Field("novel_id") novel_id: Long
    ): Call<ResponseBody>

    @GET("/v2/novel/detail")
    fun novelDetail(
        @Header("Authorization") authorization: String,
        @Query("novel_id") novel_id: Long
    ): Call<ResponseBody>

    @GET("/v1/novel/follow")
    fun novelFollow(
        @Header("Authorization") authorization: String,
        @Query("restrict") restrict: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("v1/novel/marker/add")
    fun novelMarkerAdd(
        @Header("Authorization") authorization: String,
        @Field("novel_id") novel_id: Long,
        @Field("page") page: Int
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("v1/novel/marker/delete")
    fun novelMarkerDelete(
        @Header("Authorization") authorization: String,
        @Field("novel_id") novel_id: Long
    ): Call<ResponseBody>

    @GET("v2/novel/markers")
    fun novelMarkers(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @GET("/v1/novel/mypixiv")
    fun novelMyPixiv(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @GET("/v1/novel/new")
    fun novelNew(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @GET("/v1/novel/ranking")
    fun novelRanking(
        @Header("Authorization") authorization: String,
        @Query("mode") mode: String,
        @Query("date") date: String? = null
    ): Call<ResponseBody>

    @GET("/v1/novel/recommended")
    fun novelRecommended(
        @Header("Authorization") authorization: String,
        @Query("include_ranking_novels") include_ranking_novels: Boolean = false,
        @Query("include_privacy_policy") include_privacy_policy: Boolean = false
    ): Call<ResponseBody>

    @GET("/v2/novel/series")
    fun novelSeries(
        @Header("Authorization") authorization: String,
        @Query("series_id") series_id: Long
    ): Call<ResponseBody>

    @GET("/v1/novel/text")
    fun novelText(
        @Header("Authorization") authorization: String,
        @Query("novel_id") novel_id: Long
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("v1/ppoint/android/add")
    fun ppointAndroidAdd(
        @Header("Authorization") authorization: String,
        @Field("purchase_data") purchase_data: String,
        @Field("signature") signature: String
    ): Call<ResponseBody>

    @GET("v1/ppoint/android/products")
    fun ppointAndroidProducts(
    ): Call<ResponseBody>

    @GET("v1/ppoint/can-purchase")
    fun ppointCanPurchase(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @GET("v1/ppoint/expirations")
    fun ppointExpirations(
        @Header("Authorization") authorization: String,
        @Query("platform") platform: String
    ): Call<ResponseBody>

    @GET("v1/ppoint/gains")
    fun ppointGain(
        @Header("Authorization") authorization: String,
        @Query("platform") platform: String
    ): Call<ResponseBody>

    @GET("v1/ppoint/losses")
    fun ppointLosses(
        @Header("Authorization") authorization: String,
        @Query("platform") platform: String
    ): Call<ResponseBody>

    @GET("v1/ppoint/summary")
    fun ppointSummary(
        @Header("Authorization") authorization: String,
        @Query("platform") platform: String
    ): Call<ResponseBody>

    @GET("/v1/premium/android/landing-page-url")
    fun premiumAndroidLandingPageUrl(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/premium/android/register")
    fun premiumAndroidRegister(
        @Header("Authorization") authorization: String,
        @Field("purchase_data") purchase_data: String,
        @Field("signature") signature: String,
        @Field("app_version") app_version: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("v1/privacy-policy/agreement")
    fun privacyPolicyAgreement(
        @Header("Authorization") authorization: String,
        @Field("agreement") agreement: String,
        @Field("version") version: String
    ): Call<ResponseBody>

    /**
     * 关键字自动补全
     *
     * @param authorization 令牌
     * @param word 搜索内容
     * @param merge_plain_keyword_results 合并简单的关键字结果
     */
    @GET("/v2/search/autocomplete")
    fun searchAutoComplete(
        @Header("Authorization") authorization: String,
        @Query("word") word: String,
        @Query("merge_plain_keyword_results") merge_plain_keyword_results: Boolean = true
    ): Call<ResponseBody>

    @GET("/v1/search/bookmark-ranges/illust")
    fun searchBookmarkRangesIllustration(
        @Header("Authorization") authorization: String,
        @Query("word") word: String,
        @Query("search_target") search_target: String? = null,
        @Query("start_date") start_date: String? = null,
        @Query("end_date") end_date: String? = null,
        @Query("filter") filter: String = Filter.ANDROID.toString(),
        @Query("merge_plain_keyword_results") merge_plain_keyword_results: Boolean = true,
        @Query("include_translated_tag_results") include_translated_tag_results: Boolean = true
    ): Call<ResponseBody>

    @GET("/v1/search/bookmark-ranges/novel")
    fun searchBookmarkRangesNovel(
        @Header("Authorization") authorization: String,
        @Query("word") word: String,
        @Query("search_target") search_target: String? = null,
        @Query("start_date") start_date: String? = null,
        @Query("end_date") end_date: String? = null,
        @Query("filter") filter: String = Filter.ANDROID.toString(),
        @Query("merge_plain_keyword_results") merge_plain_keyword_results: Boolean = true,
        @Query("include_translated_tag_results") include_translated_tag_results: Boolean = true
    ): Call<ResponseBody>

    /**
     * 搜索插图
     *
     * @param authorization 令牌
     * @param word 搜索内容
     * @param sort 排序方式
     * @param search_target 搜索目标
     * @param bookmark_num_max 书签编号上限
     * @param bookmark_num_min 书签编号最小值
     * @param start_date 开始日期 yyyy-MMM-dd
     * @param end_date 结束日期 yyyy-MMM-dd
     * @param filter 过滤
     * @param merge_plain_keyword_results 合并简单的关键字结果
     * @param include_translated_tag_results 包含翻译的 tag 结果
     */
    @GET("/v1/search/illust")
    fun searchIllustration(
        @Header("Authorization") authorization: String,
        @Query("word") word: String,
        @Query("sort") sort: String,
        @Query("search_target") search_target: String,
        @Query("bookmark_num_min") bookmark_num_min: Long? = null,
        @Query("bookmark_num_max") bookmark_num_max: Long? = null,
        @Query("start_date") start_date: String? = null,
        @Query("end_date") end_date: String? = null,
        @Query("filter") filter: String = Filter.ANDROID.toString(),
        @Query("merge_plain_keyword_results") merge_plain_keyword_results: Boolean = true,
        @Query("include_translated_tag_results") include_translated_tag_results: Boolean = true
    ): Call<ResponseBody>

    /**
     * 搜索小说
     *
     * @param authorization 令牌
     * @param word 搜索内容
     * @param sort 排序方式
     * @param search_target 搜索目标
     * @param bookmark_num_max 书签编号上限
     * @param bookmark_num_min 书签编号最小值
     * @param start_date 开始日期 yyyy-MMM-dd
     * @param end_date 结束日期 yyyy-MMM-dd
     * @param filter 过滤
     * @param merge_plain_keyword_results 合并简单的关键字结果
     * @param include_translated_tag_results 包含翻译的 tag 结果
     */
    @GET("/v1/search/novel")
    fun searchNovel(
        @Header("Authorization") authorization: String,
        @Query("word") word: String,
        @Query("sort") sort: String,
        @Query("search_target") search_target: String,
        @Query("bookmark_num_min") bookmark_num_min: Long? = null,
        @Query("bookmark_num_max") bookmark_num_max: Long? = null,
        @Query("start_date") start_date: String? = null,
        @Query("end_date") end_date: String? = null,
        @Query("filter") filter: String = Filter.ANDROID.toString(),
        @Query("merge_plain_keyword_results") merge_plain_keyword_results: Boolean = true,
        @Query("include_translated_tag_results") include_translated_tag_results: Boolean = true
    ): Call<ResponseBody>

    /**
     * 热门搜索
     *
     * @param authorization 令牌
     * @param word 搜索内容
     * @param search_target 搜索目标 yyyy-MMM-dd
     * @param start_date k开始日期
     * @param end_date 结束日期
     * @param filter 过滤
     * @param merge_plain_keyword_results 合并简单的关键字结果
     * @param include_translated_tag_results 包含翻译的 tag 结果
     */
    @GET("/v1/search/popular-preview/illust")
    fun searchPopularPreviewIllustration(
        @Header("Authorization") authorization: String,
        @Query("word") word: String,
        @Query("search_target") search_target: String? = null,
        @Query("start_date") start_date: String? = null,
        @Query("end_date") end_date: String? = null,
        @Query("filter") filter: String = Filter.ANDROID.toString(),
        @Query("merge_plain_keyword_results") merge_plain_keyword_results: Boolean = true,
        @Query("include_translated_tag_results") include_translated_tag_results: Boolean = true
    ): Call<ResponseBody>

    /**
     * 热门搜索
     *
     * @param authorization 令牌
     * @param word 搜索内容
     * @param search_target 搜索目标 yyyy-MMM-dd
     * @param start_date k开始日期
     * @param end_date 结束日期
     * @param filter 过滤
     * @param merge_plain_keyword_results 合并简单的关键字结果
     * @param include_translated_tag_results 包含翻译的 tag 结果
     */
    @GET("/v1/search/popular-preview/novel")
    fun searchPopularPreviewNovel(
        @Header("Authorization") authorization: String,
        @Query("word") word: String,
        @Query("search_target") search_target: String? = null,
        @Query("start_date") start_date: String?,
        @Query("end_date") end_date: String?,
        @Query("filter") filter: String = Filter.ANDROID.toString(),
        @Query("merge_plain_keyword_results") merge_plain_keyword_results: Boolean = true,
        @Query("include_translated_tag_results") include_translated_tag_results: Boolean = true
    ): Call<ResponseBody>

    /**
     * 搜索用户
     *
     * @param authorization 令牌
     * @param word 搜索内容
     * @param filter 过滤
     */
    @GET("/v1/search/user")
    fun searchUser(
        @Header("Authorization") authorization: String,
        @Query("word") word: String,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @GET("/v1/spotlight/articles")
    fun spotlightArticles(
        @Header("Authorization") authorization: String,
        @Query("category") category: String,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @GET("/v1/trending-tags/illust")
    fun trendingTagsIllustration(
        @Header("Authorization") authorization: String,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @GET("/v1/trending-tags/novel")
    fun trendingTagsNovel(
        @Header("Authorization") authorization: String,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @GET("v1/ugoira/metadata")
    fun ugoiraMetadata(
        @Header("Authorization") authorization: String,
        @Query("illust_id") illust_id: Long
    ): Call<ResponseBody>

    @POST("v1/upload/illust")
    fun uploadIllustration(
        @Header("Authorization") authorization: String,
        @Body body: RequestBody
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("v1/upload/novel")
    fun uploadNovel(
        @Header("Authorization") authorization: String,
        @Field("draft_id") draft_id: Long,
        @Field("title") title: String,
        @Field("caption") caption: String,
        @Field("cover_id") cover_id: Int,
        @Field("text") text: String,
        @Field("restrict") restrict: String,
        @Field("x_restrict") x_restrict: String,
        @Field("tags[]") tags: List<String>,
        @Field("is_original") is_original: Int
    ): Call<ResponseBody>

    @GET("v1/upload/novel/covers")
    fun uploadNovelCovers(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/upload/novel/draft")
    fun uploadNovelDraft(
        @Header("Authorization") authorization: String,
        @Field("title") title: String,
        @Field("caption") caption: String,
        @Field("cover_id") cover_id: Int,
        @Field("text") text: String,
        @Field("restrict") restrict: String,
        @Field("x_restrict") x_restrict: String,
        @Field("tags[]") tags: List<String>,
        @Field("is_original") is_original: Int
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("v1/upload/status")
    fun uploadStatus(
        @Header("Authorization") authorization: String,
        @Field("convert_key") convert_key: String
    ): Call<ResponseBody>

    /**
     * 用户插图 tag 列表
     *
     * @param authorization 令牌
     * @param user_id id
     * @param restrict 类型
     */
    @GET("v1/user/bookmark-tags/illust")
    fun userBookmarkTagsIllustration(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long,
        @Query("restrict") restrict: String
    ): Call<ResponseBody>

    @GET("v1/user/bookmark-tags/novel")
    fun userBookmarkTagsNovel(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long,
        @Query("restrict") restrict: String
    ): Call<ResponseBody>

    @GET("/v1/user/bookmarks/illust")
    fun userBookmarksIllustration(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long,
        @Query("restrict") restrict: String,
        @Query("tag") tag: String
    ): Call<ResponseBody>

    @GET("/v1/user/bookmarks/novel")
    fun userBookmarksNovel(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long,
        @Query("restrict") restrict: String,
        @Query("tag") tag: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v2/user/browsing-history/illust/add")
    fun userBrowsingHistoryIllustrationAdd(
        @Header("Authorization") authorization: String,
        @Field("illust_ids[]") illust_ids: List<Long>
    ): Call<ResponseBody>

    @GET("/v1/user/browsing-history/illusts")
    fun userBrowsingHistoryIllustrations(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v2/user/browsing-history/novel/add")
    fun userBrowsingHistoryNovelAdd(
        @Header("Authorization") authorization: String,
        @Field("novel_ids[]") novel_ids: List<Long>
    ): Call<ResponseBody>

    @GET("/v1/user/browsing-history/novels")
    fun userBrowsingHistoryNovels(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>


    /**
     * 用户详细信息
     *
     * @param authorization 令牌
     * @param user_id id
     * @param filter 过滤
     */
    @GET("/v1/user/detail")
    fun userDetail(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    /**
     * 关注用户
     *
     * @param authorization 令牌
     * @param user_id id
     * @param restrict 类型
     */
    @FormUrlEncoded
    @POST("/v1/user/follow/add")
    fun userFollowAdd(
        @Header("Authorization") authorization: String,
        @Field("user_id") user_id: Long,
        @Field("restrict") restrict: String
    ): Call<ResponseBody>

    /**
     * 取消关注用户
     *
     * @param authorization 令牌
     * @param user_id id
     */
    @FormUrlEncoded
    @POST("/v1/user/follow/delete")
    fun userFollowDelete(
        @Header("Authorization") authorization: String,
        @Field("user_id") user_id: Long
    ): Call<ResponseBody>

    /**
     * 关于该用户的关注信息
     *
     * @param authorization 令牌
     * @param user_id id
     */
    @GET("/v1/user/follow/detail")
    fun userFollowDetail(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long
    ): Call<ResponseBody>

    /**
     * 用户粉丝信息
     *
     * @param authorization 令牌
     * @param user_id id
     * @param filter 过滤
     */
    @GET("/v1/user/follower")
    fun userFollower(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    /**
     * 用户关注信息
     *
     * @param authorization 令牌
     * @param user_id id
     * @param restrict 类型
     * @param filter 过滤
     */
    @GET("/v1/user/following")
    fun userFollowing(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long,
        @Query("restrict") restrict: String,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @GET("/v1/user/illust-series")
    fun userIllustrationSeries(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long
    ): Call<ResponseBody>

    /**
     * 用户作品信息
     *
     * @param authorization 令牌
     * @param user_id id
     * @param type 类型
     * @param filter 过滤
     */
    @GET("/v1/user/illusts")
    fun userIllustration(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long,
        @Query("type") type: String,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @GET("/v1/user/me/audience-targeting")
    fun userMeAudienceTargeting(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    /**
     * 用户状态信息
     *
     * @param authorization 令牌
     */
    @GET("/v1/user/me/state")
    fun userMeState(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @GET("/v1/user/mypixiv")
    fun userMyPixiv(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/user/novel/draft/delete")
    fun userNovelDraftDelete(
        @Header("Authorization") authorization: String,
        @Field("draft_id") draft_id: Long
    ): Call<ResponseBody>

    @GET("/v1/user/novel/draft/detail")
    fun userNovelDraftDetail(
        @Header("Authorization") authorization: String,
        @Query("draft_id") draft_id: Long
    ): Call<ResponseBody>

    @GET("/v1/user/novel-draft-previews")
    fun userNovelDraftPreviews(
        @Header("Authorization") authorization: String = Settings.authorization
    ): Call<ResponseBody>

    @GET("/v1/user/novels")
    fun userNovels(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: Long
    ): Call<ResponseBody>

    @POST("/v1/user/profile/edit")
    fun userProfileEdit(
        @Header("Authorization") authorization: String,
        @Body body: RequestBody
    ): Call<ResponseBody>

    @GET("/v1/user/profile/presets")
    fun userProfilePresets(): Call<ResponseBody>

    @GET("/v1/user/recommended")
    fun userRecommended(
        @Header("Authorization") authorization: String,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @GET("/v1/user/related")
    fun userRelated(
        @Header("Authorization") authorization: String,
        @Query("seed_user_id") seed_user_id: Long,
        @Query("filter") filter: String = Filter.ANDROID.toString()
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/user/workspace/edit")
    fun userWorkspaceEdit(
        @Header("Authorization") authorization: String,
        @Field("pc") pc: String? = null,
        @Field("monitor") monitor: String? = null,
        @Field("tool") tool: String? = null,
        @Field("scanner") scanner: String? = null,
        @Field("tablet") tablet: String? = null,
        @Field("mouse") mouse: String? = null,
        @Field("printer") printer: String? = null,
        @Field("desktop") desktop: String? = null,
        @Field("music") music: String? = null,
        @Field("desk") desk: String? = null,
        @Field("chair") chair: String? = null,
        @Field("comment") comment: String? = null
    ): Call<ResponseBody>

    /**
     * 画廊
     */
    @GET("v1/walkthrough/illusts")
    fun walkThroughIllustrations(
    ): Call<ResponseBody>

    /**
     * 获取 url 内容
     *
     * @param authorization 令牌
     * @param url 网址
     */
    @GET
    fun submit(
        @Header("Authorization") authorization: String,
        @Url url: String
    ): Call<ResponseBody>

}
