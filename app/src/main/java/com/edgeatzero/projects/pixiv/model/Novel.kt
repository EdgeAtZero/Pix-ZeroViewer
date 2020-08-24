package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.DiffUtil
import com.edgeatzero.projects.pixiv.BR
import com.edgeatzero.projects.pixiv.model.util.BindingUtils
import com.edgeatzero.projects.pixiv.model.util.DataFormatUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Novel(
    @SerializedName("caption")
    var caption: String = "",
    @SerializedName("create_date")
    var createDate: String = "",
    @SerializedName("id")
    override var id: Long = 0,
    @SerializedName("image_urls")
    var imageUrls: ImageUrls = ImageUrls(),
    @SerializedName("is_bookmarked")
    var isBookmarked: Boolean = false,
    @SerializedName("is_muted")
    var isMuted: Boolean = false,
    @SerializedName("is_mypixiv_only")
    var isMypixivOnly: Boolean = false,
    @SerializedName("is_original")
    var isOriginal: Boolean = false,
    @SerializedName("is_x_restricted")
    var isXRestricted: Boolean = false,
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("restrict")
    var restrict: Long = 0,
    @SerializedName("series")
    var series: Series = Series(),
    @SerializedName("tags")
    var tags: List<Tag> = listOf(),
    @SerializedName("text_length")
    var textLength: Long = 0,
    @SerializedName("title")
    var originalTitle: String = "",
    @SerializedName("total_bookmarks")
    var totalBookmarks: Long = 0,
    @SerializedName("total_comments")
    var totalComments: Long = 0,
    @SerializedName("total_view")
    var totalView: Long = 0,
    @SerializedName("user")
    var user: User = User(),
    @SerializedName("visible")
    var visible: Boolean = false,
    @SerializedName("x_restrict")
    var xRestrict: Long = 0
) : BaseObservable(), Parcelable, BindingUtils.IdHolder, BindingUtils.IsLikeHolder {

    val title: String
        get() = when {
            originalTitle.isBlank() -> "無題"
            else -> originalTitle
        }

    val author: String
        get() = user.name

    val headPicture: String
        get() = user.profileImageUrls.medium

    val isSeriesNull: Boolean
        get() = series.id == 0L

    val isCaptionBlank: Boolean
        get() = caption.isBlank()

    override var isLiked: Boolean
        @Bindable
        get() = isBookmarked
        set(value) {
            if (isBookmarked == value) return
            if (value) totalBookmarks++ else totalBookmarks--
            isBookmarked = value
            notifyPropertyChanged(BR.liked)
            notifyPropertyChanged(BR.likeCount)
        }

    val textCount: String
        get() = DataFormatUtils.formatLongNumber(textLength)

    val viewCount: String
        get() = DataFormatUtils.formatLongNumber(totalView)

    val likeCount: String
        @Bindable
        get() = DataFormatUtils.formatLongNumber(totalBookmarks)

    val commentaryCount: String
        get() = DataFormatUtils.formatLongNumber(totalComments)

    val date: String
        get() = DataFormatUtils.formatPixivDate(createDate)

    companion object {

        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Novel>() {

            override fun areItemsTheSame(
                oldItem: Novel,
                newItem: Novel
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Novel,
                newItem: Novel
            ) = oldItem == newItem

        }


    }

    @Parcelize
    data class ImageUrls(
        @SerializedName("large")
        var large: String = "",
        @SerializedName("medium")
        var medium: String = "",
        @SerializedName("square_medium")
        var squareMedium: String = ""
    ) : Parcelable

}
