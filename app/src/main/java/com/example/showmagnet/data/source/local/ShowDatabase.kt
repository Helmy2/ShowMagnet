package com.example.showmagnet.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.showmagnet.data.source.local.converters.CategoryConverters
import com.example.showmagnet.data.source.local.converters.DateConverter
import com.example.showmagnet.data.source.local.converters.MediaTypeConverters
import com.example.showmagnet.data.source.local.converters.TimeWidowConverters
import com.example.showmagnet.data.source.local.model.CategoryDb
import com.example.showmagnet.data.source.local.model.PersonDb
import com.example.showmagnet.data.source.local.model.ShowDb
import com.example.showmagnet.domain.model.common.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        PersonDb::class,
        ShowDb::class,
        CategoryDb::class,
    ],
    version = 3,
    exportSchema = false,
)
@TypeConverters(
    value = [
        DateConverter::class,
        TimeWidowConverters::class,
        CategoryConverters::class,
        MediaTypeConverters::class
    ]
)
abstract class ShowDatabase : RoomDatabase() {
    abstract fun showDao(): ShowDao
    abstract fun personDao(): PersonDao

    companion object {
        @Volatile
        private var INSTANCE: ShowDatabase? = null

        fun getInstance(context: Context): ShowDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ShowDatabase::class.java,
                "show_magnet.db"
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val scope = MainScope()
                    scope.launch(Dispatchers.IO) {
                        getInstance(context).showDao()
                            .insertCategory(Category.values().map { CategoryDb(it) })
                    }
                }
            }).fallbackToDestructiveMigration()
                .build()
    }
}