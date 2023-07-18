package com.example.showmagnet.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.showmagnet.data.source.local.converters.DateConverter
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb

@Database(
    entities = [PersonDb::class, ShowDb::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    value = [DateConverter::class]
)
abstract class ShowDatabase : RoomDatabase() {
    abstract fun showDao(): ShowDao
    abstract fun personDao(): PersonDao

    companion object {
        @Volatile
        private var INSTANCE: ShowDatabase? = null

        fun getInstance(context: Context): ShowDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ShowDatabase::class.java, "show_magnet.db"
        ).fallbackToDestructiveMigration().build()
    }
}