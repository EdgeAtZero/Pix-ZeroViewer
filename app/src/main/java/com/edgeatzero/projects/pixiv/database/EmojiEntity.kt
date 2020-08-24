package com.edgeatzero.projects.pixiv.database

import androidx.core.text.toSpannable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.edgeatzero.library.ext.fromJson
import com.edgeatzero.library.ext.toJson
import com.edgeatzero.projects.pixiv.model.EmojiDefinition
import com.edgeatzero.projects.pixiv.model.util.EmojiUtils
import java.io.ByteArrayInputStream
import java.io.InputStream

@TypeConverters(EmojiEntity.TypeConverters::class)
@Entity(tableName = "emoji")
data class EmojiEntity(
    @ColumnInfo(name = "definition") val definition: EmojiDefinition,
    @ColumnInfo(name = "bytes", typeAffinity = ColumnInfo.BLOB) val bytes: ByteArray,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Long = definition.id
) {

    val stream: InputStream
        get() = ByteArrayInputStream(bytes)

    val text: CharSequence
        get() {
            val source = definition.slug.toSpannable()
            return EmojiUtils.load(source = source, stream = stream)
        }

    class TypeConverters {

        @TypeConverter
        fun storeUserToString(value: EmojiDefinition): String = value.toJson()

        @TypeConverter
        fun storeStringToUser(value: String): EmojiDefinition = value.fromJson()

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmojiEntity

        if (definition != other.definition) return false
        @Suppress("DEPRECATION")
        if (!bytes.contentEquals(other.bytes)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = definition.hashCode()
        @Suppress("DEPRECATION")
        result = 31 * result + bytes.contentHashCode()
        result = 31 * result + id.hashCode()
        return result
    }

    companion object {

        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EmojiEntity>() {

            override fun areItemsTheSame(oldItem: EmojiEntity, newItem: EmojiEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EmojiEntity, newItem: EmojiEntity): Boolean {
                return oldItem == newItem
            }

        }

    }

}
