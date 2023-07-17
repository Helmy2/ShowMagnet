package com.example.showmagnet.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.showmagnet.domain.model.common.TimeWindow
import java.time.LocalDateTime

@Entity(
    tableName = "person_table"
)
data class PersonDb(
    val id: Int,
    val name: String,
    val profilePath: String,
    val timeWindowDb: TimeWindow,
    val addedAt: LocalDateTime,
) {
    @PrimaryKey
    var personId: String = "$id$timeWindowDb"
}