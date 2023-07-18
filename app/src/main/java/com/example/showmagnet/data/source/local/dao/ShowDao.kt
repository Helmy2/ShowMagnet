package com.example.showmagnet.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.showmagnet.data.source.local.model.ShowDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShow(data: List<ShowDb>)

    @Query("SELECT * FROM show_table where type = :type and mediaType = :mediaType")
    fun getCategory(type: String, mediaType: String): Flow<List<ShowDb>>


    @Query("DELETE FROM show_table where type = :type and mediaType = :mediaType")
    suspend fun deleteCategory(type: String, mediaType: String)
}