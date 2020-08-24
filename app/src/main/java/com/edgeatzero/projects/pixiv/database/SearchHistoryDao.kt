package com.edgeatzero.projects.pixiv.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SearchHistoryDao {

    @Query("select * from search_histories order by timestamp desc")
    fun query(): List<SearchHistoryEntity>

    @Query("select * from search_histories where keyword like :keyword order by timestamp desc")
    fun query(keyword: String): List<SearchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entityBrowsing: SearchHistoryEntity): List<Long>

    @Query("DELETE FROM search_histories")
    fun delete()

}
