package com.example.showmagnet.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "person_table")
data class PersonDb(
    val id: Int,
    val name: String,
    val profilePath: String,
    val addedAt: LocalDateTime,
    val timeWindow: String,
    val type: String,
) {
    @PrimaryKey
    var personId: String = "$id$type$timeWindow"
}