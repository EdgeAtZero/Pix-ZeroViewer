package com.edgeatzero.projects.pixiv.database

import androidx.room.*

@Dao
interface EmojiDao {

    @Query("select * from emoji where id = :id limit 1 offset 0")
    fun query(id: Long): EmojiEntity?

    @Query("select * from emoji")
    fun queryAll(): List<EmojiEntity>

    @Query("select count(*) from emoji")
    fun queryCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: EmojiEntity): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<EmojiEntity>): List<Long>

    @Delete
    fun delete(vararg entity: EmojiEntity): Int

    @Query("DELETE FROM emoji")
    fun deleteAll()

}
