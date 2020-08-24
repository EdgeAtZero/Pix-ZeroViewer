package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Workspace(
    @SerializedName("chair")
    var chair: String = "",
    @SerializedName("comment")
    var comment: String = "",
    @SerializedName("desk")
    var desk: String = "",
    @SerializedName("desktop")
    var desktop: String = "",
    @SerializedName("monitor")
    var monitor: String = "",
    @SerializedName("mouse")
    var mouse: String = "",
    @SerializedName("music")
    var music: String = "",
    @SerializedName("pc")
    var pc: String = "",
    @SerializedName("printer")
    var printer: String = "",
    @SerializedName("scanner")
    var scanner: String = "",
    @SerializedName("tablet")
    var tablet: String = "",
    @SerializedName("tool")
    var tool: String = "",
    @SerializedName("workspace_image_url")
    var workspaceImageUrl: String = "" // null
) : Parcelable
