package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.person.PersonDetails

interface PersonRepository {
    suspend fun getPersonDetails(id: Int): Result<PersonDetails>

    suspend fun getImages(id: Int): Result<List<Image>>

    suspend fun getMovieCredits(id: Int): Result<List<Show>>

    suspend fun getTvCredits(id: Int): Result<List<Show>>
}