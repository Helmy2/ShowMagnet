package com.example.showmagnet.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.showmagnet.data.source.local.model.CategoryDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.domain.model.common.Category
import com.example.showmagnet.domain.model.common.MediaType

@Dao
interface ShowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShow(data: List<ShowDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(data: List<CategoryDb>)

    @Query("SELECT * FROM category_table join show_table on category = categoryName where category = :category and mediaType = :mediaType")
    fun getCategory(category: Category, mediaType: MediaType): List<ShowDb>


    @Query("DELETE FROM show_table where categoryName = :category and mediaType = :mediaType")
    suspend fun deleteCategory(category: Category, mediaType: MediaType)
}