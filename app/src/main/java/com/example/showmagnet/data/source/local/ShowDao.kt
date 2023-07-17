package com.example.showmagnet.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.showmagnet.data.source.local.model.CategoryDb
import com.example.showmagnet.data.source.local.model.CategoryWithShows
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.domain.model.common.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShow(data: List<ShowDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(data: List<CategoryDb>)

    @Transaction
    @Query("SELECT * FROM category_table where category = :category")
    fun getCategory(category: Category): Flow<List<CategoryWithShows>>


}