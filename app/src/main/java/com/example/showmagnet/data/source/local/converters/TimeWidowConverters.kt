package com.example.showmagnet.data.source.local.converters

import androidx.room.TypeConverter
import com.example.showmagnet.domain.model.common.TimeWindow

class TimeWidowConverters {
    @TypeConverter
    fun fromTimeWidow(timeWindow: TimeWindow): String {
        return timeWindow.name
    }

    fun toTimeWidow(timeWindow: String): TimeWindow {
        return TimeWindow.valueOf(timeWindow)
    }
}