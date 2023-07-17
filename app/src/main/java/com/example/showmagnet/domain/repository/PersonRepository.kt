package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.common.TimeWindow
import com.example.showmagnet.domain.model.person.Person
import com.example.showmagnet.domain.model.person.PersonDetails
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun getPersonDetails(id: Int): Result<PersonDetails>

    suspend fun getImages(id: Int): Result<List<Image>>

    suspend fun getMovieCredits(id: Int): Result<List<Show>>

    suspend fun getTvCredits(id: Int): Result<List<Show>>
    fun getTrendingPeople(timeWindow: TimeWindow): Flow<Result<List<Person>>>
    suspend fun addPersonToFavoriteList(id: Int): Result<Boolean>
    suspend fun getPersonFavoriteList(): Result<List<Int>>
    suspend fun deleteFromFavoritePersonsList(id: Int): Result<Boolean>
    suspend fun isFavoritePersons(id: Int): Result<Boolean>
    suspend fun getFavoritePeople(): Result<List<Person>>
}