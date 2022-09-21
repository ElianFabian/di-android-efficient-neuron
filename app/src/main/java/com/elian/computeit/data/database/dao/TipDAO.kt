package com.elian.computeit.data.database.dao

import androidx.room.*
import com.elian.computeit.data.model.Tip

@Dao
interface TipDAO
{
    @Query("SELECT * FROM tip_table WHERE id = :id")
    fun select(id: Long): Tip

    @Query("SELECT * FROM tip_table")
    fun selectAll(): List<Tip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tip: Tip): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(tip: Tip)

    @Delete
    fun delete(tip: Tip)

    @Query("DELETE FROM tip_table")
    fun deleteAll()
}