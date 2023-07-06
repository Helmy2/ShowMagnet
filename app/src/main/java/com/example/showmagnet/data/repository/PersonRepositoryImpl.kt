package com.example.showmagnet.data.repository

import com.example.showmagnet.data.source.remote.api.PersonApi
import com.example.showmagnet.data.source.remote.model.person.toPersonDetails
import com.example.showmagnet.data.source.remote.model.toImage
import com.example.showmagnet.data.source.remote.model.show.toShow
import com.example.showmagnet.domain.model.Image
import com.example.showmagnet.domain.model.MediaType
import com.example.showmagnet.domain.model.Show
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.domain.repository.PersonRepository
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val api: PersonApi,
) : PersonRepository {
    override suspend fun getPersonDetails(id: Int): Result<PersonDetails> = try {
        val response = api.getPersonDetails(id).toPersonDetails()
        Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getImages(id: Int): Result<List<Image>> = try {
        val response = api.getPersonImages(id).profiles.map { it.toImage() }
        Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getMovieCredits(id: Int): Result<List<Show>> = try {
        val response = api.getMovieCredits(id)
        val result = ((response.cast ?: emptyList()) + (response.crow ?: emptyList())).map {
            it.toShow(
                MediaType.MOVIE
            )
        }
        Result.success(result)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getTvCredits(id: Int): Result<List<Show>> = try {
        val response = api.getTvCredits(id)
        val result = ((response.cast ?: emptyList()) + (response.crow ?: emptyList())).map {
            it.toShow(MediaType.TV)
        }
        Result.success(result)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}
