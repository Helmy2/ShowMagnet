package com.example.showmagnet.data.source.local.converters

import androidx.room.TypeConverter
import com.example.showmagnet.domain.model.common.MediaType

class MediaTypeConverters {
    @TypeConverter
    fun fromMediaType(mediaType: MediaType): String {
        return mediaType.name
    }

    fun toMediaType(mediaType: String): MediaType {
        return MediaType.valueOf(mediaType)
    }
}