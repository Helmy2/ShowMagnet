package com.example.showmagnet.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.showmagnet.domain.model.common.Category

@Entity(
    tableName = "category_table"
)
data class CategoryDb(
    @PrimaryKey
    val category: Category,
)
