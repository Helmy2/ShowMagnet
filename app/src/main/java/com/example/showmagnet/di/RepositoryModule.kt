package com.example.showmagnet.di

import com.example.showmagnet.data.repository.MovieRepositoryImpl
import com.example.showmagnet.data.repository.PersonRepositoryImpl
import com.example.showmagnet.data.repository.ShowRepositoryImpl
import com.example.showmagnet.data.repository.TvRepositoryImpl
import com.example.showmagnet.data.repository.UserRepositoryImpl
import com.example.showmagnet.domain.repository.MovieRepository
import com.example.showmagnet.domain.repository.PersonRepository
import com.example.showmagnet.domain.repository.ShowRepository
import com.example.showmagnet.domain.repository.TvRepository
import com.example.showmagnet.domain.repository.UserRepository
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
    abstract fun bindShowRepository(
        showRepositoryImpl: ShowRepositoryImpl
    ): ShowRepository

    @Binds
    @Singleton
    abstract fun bindMovieDetailsRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindPersonDetailsRepository(
        personRepositoryImpl: PersonRepositoryImpl
    ): PersonRepository

    @Binds
    @Singleton
    abstract fun bindTvDetailsRepository(
        tvRepositoryImpl: TvRepositoryImpl
    ): TvRepository


    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

}