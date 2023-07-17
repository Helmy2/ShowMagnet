package com.example.showmagnet.data.source.local.converters

import androidx.room.TypeConverter
import com.example.showmagnet.utils.toLocalDateTime
import com.example.showmagnet.utils.toMillis
import java.time.LocalDateTime

class DateConverter {
    @TypeConverter
    fun toDate(date: Long): LocalDateTime {

        return date.toLocalDateTime()
    }

    @TypeConverter
    fun toDateLong(date: LocalDateTime): Long {

        return date.toMillis()
    }
}

