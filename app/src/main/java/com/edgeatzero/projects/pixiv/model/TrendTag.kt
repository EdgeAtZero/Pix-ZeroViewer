package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrendTag(
    @SerializedName("illust")
    var illust: Illustration,
    @SerializedName("tag")
    var tag: String = "",
    @SerializedName("translated_name")
    var translatedName: String? = ""
) : Parcelable {

    val hasTranslatedName
        get() = translatedName.isNullOrBlank().not()

    companion object {

        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TrendTag>() {

            override fun areItemsTheSame(
                oldItem: TrendTag,
                newItem: TrendTag
            ) = oldItem.tag == newItem.tag

            override fun areContentsTheSame(
                oldItem: TrendTag,
                newItem: TrendTag
            ) = oldItem == newItem

        }

    }

}
