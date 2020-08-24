package com.edgeatzero.projects.pixiv.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class SearchDuration(val value: Long) : Parcelable {

    CUSTOM_DURATION(2131689980),

    ALL(2131689979),

    WITHIN_LAST_DAY(2131689984),

    WITHIN_LAST_WEEK(2131689987),

    WITHIN_LAST_MONTH(2131689986),

    WITHIN_HALF_YEAR(2131689985),

    WITHIN_YEAR(2131689988),

    SELECT(2131689983);

    override fun toString(): String = value.toString()

}
