package com.example.showmagnet.data.source.local.converters

import androidx.room.TypeConverter
import com.example.showmagnet.domain.model.common.Category

class CategoryConverters {
    @TypeConverter
    fun fromCategory(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(value: String): Category {
        return Category.valueOf(value)
    }
}