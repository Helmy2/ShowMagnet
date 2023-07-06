package com.example.showmagnet.di

import android.content.Context
import com.example.showmagnet.common.Constants.BASE_URL
import com.example.showmagnet.data.source.remote.AuthorizationInterceptor
import com.example.showmagnet.data.source.remote.api.MovieDetailsApi
import com.example.showmagnet.data.source.remote.api.PersonApi
import com.example.showmagnet.data.source.remote.api.ShowApi
import com.example.showmagnet.ui.utils.NetworkConnectivityService
import com.example.showmagnet.ui.utils.NetworkConnectivityServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .addInterceptor(AuthorizationInterceptor)
            .build()


    @Provides
    @Singleton
    fun provideShowApi(retrofit: Retrofit): ShowApi =
        retrofit.create(ShowApi::class.java)

    @Provides
    @Singleton
    fun provideMovieDetailsApi(retrofit: Retrofit): MovieDetailsApi =
        retrofit.create(MovieDetailsApi::class.java)

    @Provides
    @Singleton
    fun providePersonApi(retrofit: Retrofit): PersonApi =
        retrofit.create(PersonApi::class.java)

    @Provides
    @Singleton
    fun provideNetworkConnectivityService(
        @ApplicationContext context: Context
    ): NetworkConnectivityService = NetworkConnectivityServiceImpl(context)
}