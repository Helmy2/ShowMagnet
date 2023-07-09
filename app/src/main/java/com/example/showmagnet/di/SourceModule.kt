package com.example.showmagnet.di

import com.example.showmagnet.data.source.remote.database.FireStoreDataSource
import com.example.showmagnet.data.source.remote.database.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {
    @Binds
    @Singleton
    abstract fun bindFireStoreDataSource(
        fireStoreDataSource: FireStoreDataSource
    ): RemoteDataSource
}