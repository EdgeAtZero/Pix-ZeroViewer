package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import android.text.Spannable
import androidx.recyclerview.widget.DiffUtil
import com.edgeatzero.projects.pixiv.model.util.BindingUtils
import com.edgeatzero.projects.pixiv.model.util.DataFormatUtils
import com.edgeatzero.projects.pixiv.model.util.EmojiUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Commentary(
    @SerializedName("comment")
    var comment: String = "",
    @SerializedName("date")
    var originalDate: String = "",
    @SerializedName("has_replies")
    var hasReplies: Boolean = false,
    @SerializedName("id")
    override var id: Long = 0,
    @SerializedName("user")
    var user: User = User()
) : Parcelable, BindingUtils.IdHolder {

    val author: String
        get() = user.name

    val headPicture: String
        get() = user.profileImageUrls.medium

    val date: String
        get() = DataFormatUtils.formatPixivDate(originalDate)

    fun span(): Spannable {
        return EmojiUtils.transform(comment, false)
    }

    companion object {

        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Commentary>() {

            override fun areItemsTheSame(
                oldItem: Commentary,
                newItem: Commentary
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Commentary,
                newItem: Commentary
            ) = oldItem == newItem

        }

    }

    @Parcelize
    data class User(
        @SerializedName("account")
        var account: String = "",
        @SerializedName("id")
        var id: Long = 0,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("profile_image_urls")
        var profileImageUrls: ProfileImageUrls = ProfileImageUrls()
    ) : Parcelable

}
