package com.edgeatzero.projects.pixiv.database

import androidx.room.*
import com.edgeatzero.library.ext.fromJsonOrNull
import com.edgeatzero.library.ext.toJsonOrNull
import com.edgeatzero.projects.pixiv.constant.ContentType
import com.edgeatzero.projects.pixiv.model.Illustration
import com.edgeatzero.projects.pixiv.model.Novel
import com.edgeatzero.projects.pixiv.model.User
import com.edgeatzero.projects.pixiv.model.util.BindingUtils

@TypeConverters(BrowsingHistoryEntity.TypeConverters::class)
@Entity(tableName = "browsing_histories")
data class BrowsingHistoryEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") override val id: Long,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "type") val type: ContentType = ContentType.ILLUSTRATION,
    @ColumnInfo(name = "illustration") val illustration: Illustration? = null,
    @ColumnInfo(name = "novel") val novel: Novel? = null,
    @ColumnInfo(name = "user") val user: User? = null
) : BindingUtils.IdHolder {

    constructor(illustration: Illustration) : this(
        id = illustration.id,
        type = if (illustration.isManga) ContentType.MANGA else ContentType.ILLUSTRATION,
        illustration = illustration
    )

    constructor(novel: Novel) : this(
        id = novel.id,
        type = ContentType.NOVEL,
        novel = novel
    )

    constructor(user: User) : this(
        id = user.id,
        type = ContentType.USER,
        user = user
    )

    class TypeConverters {

        @TypeConverter
        fun storeIllustrationToString(value: Illustration?): String? = value.toJsonOrNull()

        @TypeConverter
        fun storeStringToIllustration(value: String?): Illustration? = value.fromJsonOrNull()

        @TypeConverter
        fun storeNovelToString(value: Novel?): String? = value.toJsonOrNull()

        @TypeConverter
        fun storeStringToNovel(value: String?): Novel? = value.fromJsonOrNull()

        @TypeConverter
        fun storeUserToString(value: User?): String? = value.toJsonOrNull()

        @TypeConverter
        fun storeStringToUser(value: String?): User? = value.fromJsonOrNull()

        @TypeConverter
        fun storeContentTypeToInt(value: ContentType?): Int? = ContentType.values().indexOf(value)

        @TypeConverter
        fun storeIntToContentType(value: Int?): ContentType? = value?.let {
            try {
                ContentType.values()[it]
            } catch (e: IndexOutOfBoundsException) {
                null
            }
        }

    }

}
