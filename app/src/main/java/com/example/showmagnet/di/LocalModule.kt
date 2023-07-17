package com.example.showmagnet.di

import android.content.Context
import com.example.showmagnet.data.source.local.PersonDao
import com.example.showmagnet.data.source.local.ShowDao
import com.example.showmagnet.data.source.local.ShowDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideShowDataBase(
        @ApplicationContext context: Context,
    ): ShowDatabase {
        return ShowDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun providePersonDao(db: ShowDatabase): PersonDao {
        return db.personDao()
    }

    @Singleton
    @Provides
    fun provideShowDao(db: ShowDatabase): ShowDao {
        return db.showDao()
    }

}

