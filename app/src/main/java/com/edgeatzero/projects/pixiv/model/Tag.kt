package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tag(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("added_by_uploaded_user")
    var isAddedByUploadedUser: Boolean = false,
    @SerializedName("translated_name")
    var translatedName: String? = ""
) : Parcelable {

    val hasTranslatedName
        get() = translatedName.isNullOrBlank().not()

    companion object {

        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Tag>() {

            override fun areItemsTheSame(
                oldItem: Tag,
                newItem: Tag
            ) = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: Tag,
                newItem: Tag
            ) = oldItem == newItem

        }

    }

}
