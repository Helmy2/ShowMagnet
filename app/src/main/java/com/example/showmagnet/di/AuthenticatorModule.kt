package com.example.showmagnet.di

import android.content.Context
import com.example.showmagnet.data.repository.AuthRepositoryImpl
import com.example.showmagnet.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AuthenticatorModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideAuthenticator(
        auth: FirebaseAuth,
        @ApplicationContext context: Context
    ): AuthRepository =
        AuthRepositoryImpl(context, auth, Identity.getSignInClient(context))
}