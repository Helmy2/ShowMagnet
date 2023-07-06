package com.example.showmagnet.di

import com.example.showmagnet.data.repository.MovieDetailsRepositoryImpl
import com.example.showmagnet.data.repository.PersonRepositoryImpl
import com.example.showmagnet.data.repository.ShowRepositoryImpl
import com.example.showmagnet.domain.repository.MovieDetailsRepository
import com.example.showmagnet.domain.repository.PersonRepository
import com.example.showmagnet.domain.repository.ShowRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideShowRepository(
        showRepositoryImpl: ShowRepositoryImpl
    ): ShowRepository

    @Binds
    @Singleton
    abstract fun provideMovieDetailsRepository(
        movieDetailsRepositoryImpl: MovieDetailsRepositoryImpl
    ): MovieDetailsRepository

    @Binds
    @Singleton
    abstract fun providePersonDetailsRepository(
        personRepositoryImpl: PersonRepositoryImpl
    ): PersonRepository
}