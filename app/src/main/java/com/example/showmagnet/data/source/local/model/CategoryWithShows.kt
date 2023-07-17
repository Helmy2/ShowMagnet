package com.example.showmagnet.data.source.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithShows(
    @Embedded val category: CategoryDb,
    @Relation(
        parentColumn = "category",
        entityColumn = "categoryName"
    )
    val shows: List<ShowDb>
)