package com.edgeatzero.projects.pixiv.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.room.*

@Dao
interface AccountDao {

    @Query("select * from accounts where id = :id limit 1 offset 0")
    fun query(id: Long): AccountEntity?

    @Query("select * from accounts")
    fun queryAll(): List<AccountEntity>

    @Query("select * from accounts")
    fun queryAllAsLiveData(): LiveData<List<AccountEntity>>

    @Query("select * from accounts order by id asc")
    fun queryAllAsDataSourceOrderById(): DataSource.Factory<Int, AccountEntity>

    @Query("select * from accounts order by name asc")
    fun queryAllAsDataSourceOrderByName(): DataSource.Factory<Int, AccountEntity>

    @Query("select * from accounts where id = :id limit 1 offset 0")
    fun queryAsLiveData(id: Long): LiveData<AccountEntity>

    @Query("select count(*) from accounts")
    fun queryCount(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: AccountEntity): List<Long>

    @Delete
    fun delete(vararg entity: AccountEntity): Int

    @Query("DELETE FROM accounts")
    fun deleteAll()

}
