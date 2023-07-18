package com.example.showmagnet.data.source.remote.api

import com.example.showmagnet.data.source.remote.api.model.person.PeopleResponse
import com.example.showmagnet.data.source.remote.api.model.person.PersonCreditResponse
import com.example.showmagnet.data.source.remote.api.model.person.PersonDetailsResponse
import com.example.showmagnet.data.source.remote.api.model.person.PersonImagesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonApi {
    @GET(ApiConstants.Person.DETAILS_ENDPOINT)
    suspend fun getPersonDetails(@Path(ApiParameters.ID) id: Int): PersonDetailsResponse

    @GET(ApiConstants.Person.MOVIE_CREDITS_ENDPOINT)
    suspend fun getMovieCredits(@Path(ApiParameters.ID) id: Int): PersonCreditResponse

    @GET(ApiConstants.Person.TV_CREDITS_ENDPOINT)
    suspend fun getTvCredits(@Path(ApiParameters.ID) id: Int): PersonCreditResponse

    @GET(ApiConstants.Person.IMAGES_ENDPOINT)
    suspend fun getPersonImages(@Path(ApiParameters.ID) id: Int): PersonImagesResponse

    @GET(ApiConstants.Person.TRENDING_ENDPOINT)
    suspend fun getPopularPeople(@Path(ApiParameters.TIME_WINDOW) timeWindow: String): PeopleResponse

}

