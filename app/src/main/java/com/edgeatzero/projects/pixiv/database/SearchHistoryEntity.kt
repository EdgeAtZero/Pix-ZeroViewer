package com.edgeatzero.projects.pixiv.database

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.edgeatzero.projects.pixiv.model.util.BindingUtils

@Entity(tableName = "search_histories")
data class SearchHistoryEntity @JvmOverloads constructor(
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "keyword") val keyword: String
) : BindingUtils.IdHolder {

    override val id: Long
        get() = timestamp

    companion object {

        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchHistoryEntity>() {

            override fun areItemsTheSame(
                oldItem: SearchHistoryEntity,
                newItem: SearchHistoryEntity
            ) = oldItem.timestamp == newItem.timestamp

            override fun areContentsTheSame(
                oldItem: SearchHistoryEntity,
                newItem: SearchHistoryEntity
            ) = oldItem == newItem

        }

    }
}
