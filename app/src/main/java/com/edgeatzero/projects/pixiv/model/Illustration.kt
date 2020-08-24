package com.edgeatzero.projects.pixiv.model

import android.content.Context
import android.os.Parcelable
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.text.toSpannable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.DiffUtil
import com.edgeatzero.library.common.text.style.SimpleClickableSpan
import com.edgeatzero.library.ext.set
import com.edgeatzero.projects.pixiv.BR
import com.edgeatzero.projects.pixiv.R
import com.edgeatzero.projects.pixiv.constant.UploadIllustrationParameter
import com.edgeatzero.projects.pixiv.model.util.BindingUtils
import com.edgeatzero.projects.pixiv.model.util.DataFormatUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.jetbrains.anko.toast

@Parcelize
data class Illustration(
    @SerializedName("caption")
    var caption: String = "",
    @SerializedName("create_date")
    var createDate: String = "",
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("id")
    override var id: Long = 0,
    @SerializedName("image_urls")
    var imageUrls: ImageUrls = ImageUrls(),
    @SerializedName("is_bookmarked")
    var isBookmarked: Boolean = false,
    @SerializedName("is_muted")
    var isMuted: Boolean = false,
    @SerializedName("meta_pages")
    var originalMetaPages: List<MetaPage> = listOf(),
    @SerializedName("meta_single_page")
    var metaSinglePage: MetaSinglePage = MetaSinglePage(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("restrict")
    var restrict: Long = 0,
    @SerializedName("sanity_level")
    var sanityLevel: Long = 0,
    @SerializedName("series")
    var series: Series? = null,
    @SerializedName("tags")
    var tags: List<Tag> = listOf(),
    @SerializedName("title")
    var originalTitle: String = "",
    @SerializedName("tools")
    var tools: List<String> = listOf(),
    @SerializedName("total_bookmarks")
    var totalBookmarks: Long = 0,
    @SerializedName("total_view")
    var totalView: Long = 0,
    @SerializedName("type")
    var type: String = "",
    @SerializedName("user")
    var user: User = User(),
    @SerializedName("visible")
    var visible: Boolean = false,
    @SerializedName("width")
    var width: Int = 0,
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

    val ratio: Double
        get() = width.toDouble().div(height)

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

    val isSingleIllustration: Boolean
        get() = pageCount == 1

    val isIllustration: Boolean
        get() = type == UploadIllustrationParameter.UPLOAD_PARAMS_TYPE_ILLUSTRATION

    val isManga: Boolean
        get() = type == UploadIllustrationParameter.UPLOAD_PARAMS_TYPE_MANGA

    val isGif: Boolean
        get() = type == "ugoira"

    val viewCount: String
        get() = DataFormatUtils.formatLongNumber(totalView)

    val likeCount: String
        @Bindable
        get() = DataFormatUtils.formatLongNumber(totalBookmarks)

    val date: String
        get() = DataFormatUtils.formatPixivDate(createDate)

    val metaPage: List<MetaPage>
        get() = if (pageCount == 1) listOf(MetaPage(imageUrls.copy(original = metaSinglePage.originalImageUrl)))
        else originalMetaPages

    val size: String
        get() = "${width}x$height"

    val url: String
        get() = imageUrls.large

    fun span(context: Context): SpanHolder {
        val array = context.obtainStyledAttributes(intArrayOf(android.R.attr.colorAccent))
        val color = array.getColor(0, 0)
        array.recycle()
        return SpanHolder(
            spanView(context, color),
            spanLike(context, color),
            spanSize(context, color),
            spanId(context, color)
        )
    }

    private fun spanView(context: Context, color: Int): CharSequence {
        val p0 = "$totalView"
        val spannable = "$p0 ${context.getString(R.string.string_read)}".toSpannable()
        spannable[0, p0.length] = object : SimpleClickableSpan() {

            override fun onClick(widget: View) {
                context.toast("Not yet implemented")
            }

        }
        spannable[0, p0.length] = ForegroundColorSpan(color)
        return spannable
    }

    private fun spanLike(context: Context, color: Int): CharSequence {
        val p0 = "$totalBookmarks"
        val spannable = "$p0 ${context.getString(R.string.string_like)}".toSpannable()
        spannable[0, p0.length] = object : SimpleClickableSpan() {

            override fun onClick(widget: View) {
                context.toast("Not yet implemented")
            }

        }
        spannable[0, p0.length] = ForegroundColorSpan(color)
        return spannable
    }

    private fun spanSize(context: Context, color: Int): CharSequence {
        val p0 = "$width"
        val p1 = "x"
        val p2 = "$height"
        val spannable = "$p0$p1$p2".toSpannable()
        spannable.setSpan(
            ForegroundColorSpan(color),
            0,
            p0.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val i = p0.length + p1.length
        spannable[i, i + p2.length] = ForegroundColorSpan(color)
        return spannable
    }

    private fun spanId(context: Context, color: Int): CharSequence {
        val p0 = "ID: "
        val p1 = "$id"
        val spannable = "$p0$p1".toSpannable()
        spannable[p0.length, p0.length + p1.length] = ForegroundColorSpan(color)
        return spannable
    }

    data class SpanHolder(
        val view: CharSequence,
        val like: CharSequence,
        val size: CharSequence,
        val id: CharSequence
    )

    companion object {

        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Illustration>() {

            override fun areItemsTheSame(
                oldItem: Illustration,
                newItem: Illustration
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Illustration,
                newItem: Illustration
            ) = oldItem == newItem

        }

    }

}
