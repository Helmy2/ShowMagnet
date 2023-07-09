package com.example.showmagnet.di

import com.example.showmagnet.data.repository.AuthRepositoryImpl
import com.example.showmagnet.data.repository.MovieRepositoryImpl
import com.example.showmagnet.data.repository.PersonRepositoryImpl
import com.example.showmagnet.data.repository.ShowRepositoryImpl
import com.example.showmagnet.data.repository.TvDetailsRepositoryImpl
import com.example.showmagnet.data.repository.UserRepositoryImpl
import com.example.showmagnet.domain.repository.AuthRepository
import com.example.showmagnet.domain.repository.MovieRepository
import com.example.showmagnet.domain.repository.PersonRepository
import com.example.showmagnet.domain.repository.ShowRepository
import com.example.showmagnet.domain.repository.TvDetailsRepository
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
        tvDetailsRepositoryImpl: TvDetailsRepositoryImpl
    ): TvDetailsRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository


    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

}