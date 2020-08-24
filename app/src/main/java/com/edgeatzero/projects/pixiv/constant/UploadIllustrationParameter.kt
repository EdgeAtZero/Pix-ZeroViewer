package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class UploadIllustrationParameter(
    var ageLimit: String? = null,
    var caption: String? = null,
    var contentType: String? = null,
    var imagePathList: ArrayList<String> = ArrayList<String>(),
    var publicity: String? = null,
    var sexual: Boolean? = null,
    var tagList: ArrayList<String> = ArrayList<String>(),
    var title: String? = null
) : Parcelable {

    companion object {

        const val UPLOAD_PARAMS_AGE_LIMIT_NONE = "none"

        const val UPLOAD_PARAMS_AGE_LIMIT_R18 = "r18"

        const val UPLOAD_PARAMS_AGE_LIMIT_R18G = "r18g"

        const val UPLOAD_PARAMS_PUBLICITY_FRIEND = "mypixiv"

        const val UPLOAD_PARAMS_PUBLICITY_PRIVATE = "private"

        const val UPLOAD_PARAMS_PUBLICITY_PUBLIC = "public"

        const val UPLOAD_PARAMS_TYPE_ILLUSTRATION = "illust"

        const val UPLOAD_PARAMS_TYPE_MANGA = "manga"

    }

}
