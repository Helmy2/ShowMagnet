package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.source.remote.model.person.PersonCreditResponse
import com.example.showmagnet.data.source.remote.model.person.PersonDetailsResponse
import com.example.showmagnet.data.source.remote.model.person.PersonImagesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonApi {
    @GET("person/{id}")
    suspend fun getPersonDetails(@Path("id") id: Int): PersonDetailsResponse

    @GET("person/{id}/movie_credits")
    suspend fun getMovieCredits(@Path("id") id: Int): PersonCreditResponse

    @GET("person/{id}/tv_credits")
    suspend fun getTvCredits(@Path("id") id: Int): PersonCreditResponse

    @GET("person/{id}/images")
    suspend fun getPersonImages(@Path("id") id: Int): PersonImagesResponse
}

