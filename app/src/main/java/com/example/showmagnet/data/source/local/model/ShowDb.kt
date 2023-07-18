package com.example.showmagnet.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "show_table")
data class ShowDb(
    val id: Int,
    val title: String,
    val voteAverage: Float,
    val posterPath: String,
    val addedAt: LocalDateTime,
    val mediaType: String,
    val type: String,
) {
    @PrimaryKey
    var showId: String = "$id$type"
}