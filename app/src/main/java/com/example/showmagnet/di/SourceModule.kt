package com.example.showmagnet.di

import com.example.showmagnet.data.source.local.LocalManager
import com.example.showmagnet.data.source.local.LocalManagerImpl
import com.example.showmagnet.data.source.remote.api.RemoteManager
import com.example.showmagnet.data.source.remote.api.RemoteManagerImpl
import com.example.showmagnet.data.source.remote.database.FireStoreUserDataSource
import com.example.showmagnet.data.source.remote.database.RemoteUserDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {
    @Binds
    abstract fun bindFireStore(
        fireStoreDataSource: FireStoreUserDataSource
    ): RemoteUserDataSource

    @Binds
    abstract fun bindLocalMangerDataSource(
        localManagerImpl: LocalManagerImpl
    ): LocalManager

    @Binds
    abstract fun bindRemoteMangerDataSource(
        remoteManagerImpl: RemoteManagerImpl
    ): RemoteManager
}