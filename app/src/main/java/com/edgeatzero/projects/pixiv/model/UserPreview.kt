package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.edgeatzero.projects.pixiv.model.util.BindingUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserPreview(
    @SerializedName("illusts")
    var illusts: List<Illustration> = listOf(),
    @SerializedName("is_muted")
    var isMuted: Boolean = false,
    @SerializedName("novels")
    var novels: List<Novel> = listOf(),
    @SerializedName("user")
    var user: User = User()
) : Parcelable, BindingUtils.IdHolder {

    fun safeGetUrl(index: Int): String? {
        return try {
            illusts[index].url
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    override val id: Long
        get() = user.id

}
