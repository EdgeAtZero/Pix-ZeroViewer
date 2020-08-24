package com.edgeatzero.projects.pixiv.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface BrowsingHistoryDao {

    @Query("select * from browsing_histories where id = :id limit 1 offset 0")
    fun query(id: Long): BrowsingHistoryEntity?

    @Query("select * from browsing_histories")
    fun queryAll(): List<BrowsingHistoryEntity>

    @Query("select * from browsing_histories order by timestamp desc")
    fun queryAllOrderByTimestamp(): List<BrowsingHistoryEntity>

    @Query("select * from browsing_histories order by timestamp desc")
    fun queryAllAsDataSourceOrderByTimestamp(): DataSource.Factory<Int, BrowsingHistoryEntity>

    @Query("select * from browsing_histories where id = :id limit 1 offset 0")
    fun queryAsLiveData(id: Long): LiveData<BrowsingHistoryEntity>

    @Query("select count(*) from browsing_histories")
    fun queryCount(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entityBrowsing: BrowsingHistoryEntity): List<Long>

    @Delete
    fun delete(vararg entityBrowsing: BrowsingHistoryEntity): Int

    @Query("DELETE FROM browsing_histories")
    fun deleteAll()

}
