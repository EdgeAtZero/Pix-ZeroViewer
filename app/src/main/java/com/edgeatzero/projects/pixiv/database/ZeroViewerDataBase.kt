package com.edgeatzero.projects.pixiv.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AccountEntity::class, BrowsingHistoryEntity::class, SearchHistoryEntity::class, EmojiEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ZeroViewerDataBase : RoomDatabase() {

    abstract fun getAccountDao(): AccountDao

    abstract fun getBrowsingHistoryDao(): BrowsingHistoryDao

    abstract fun getSearchHistoryDao(): SearchHistoryDao

    abstract fun getEmojiDao(): EmojiDao

}
