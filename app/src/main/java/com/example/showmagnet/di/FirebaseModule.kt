package com.example.showmagnet.di

import android.content.Context
import com.example.showmagnet.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultWebClientId

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CurrentFirebaseUser

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore


    @Provides
    @Singleton
    @DefaultWebClientId
    fun provideDefaultWebClientId(@ApplicationContext context: Context): String =
        context.getString(R.string.default_web_client_id)

    @Provides
    @Singleton
    @CurrentFirebaseUser
    fun provideFireBaseUser(auth: FirebaseAuth): FirebaseUser? = auth.currentUser

}