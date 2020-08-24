package com.edgeatzero.projects.pixiv.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmojiDefinitionData(
    @SerializedName("emoji_definitions")
    var emojiDefinitions: List<EmojiDefinition> = listOf()
) : Parcelable
