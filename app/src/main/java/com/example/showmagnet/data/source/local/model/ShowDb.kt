package com.example.showmagnet.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.showmagnet.domain.model.common.MediaType
import java.time.LocalDateTime

@Entity(
    tableName = "show_table"
)
data class ShowDb(
    val id: Int,
    val categoryName: String,
    val title: String,
    val voteAverage: Float,
    val posterPath: String,
    val mediaType: MediaType,
    val addedAt: LocalDateTime,
) {
    @PrimaryKey
    var showId: String = "$id$categoryName"
}