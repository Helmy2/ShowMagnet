package com.example.showmagnet.data.repository

import com.example.showmagnet.data.mapper.toDomain
import com.example.showmagnet.data.source.remote.api.PersonApi
import com.example.showmagnet.domain.model.common.Image
import com.example.showmagnet.domain.model.common.MediaType
import com.example.showmagnet.domain.model.common.Show
import com.example.showmagnet.domain.model.person.PersonDetails
import com.example.showmagnet.domain.repository.PersonRepository
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val api: PersonApi,
) : PersonRepository {
    override suspend fun getPersonDetails(id: Int): Result<PersonDetails> = try {
        val response = api.getPersonDetails(id).toDomain()
        Result.success(response)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getImages(id: Int): Result<List<Image>> = try {
        val response = api.getPersonImages(id)
        val result = response.profiles?.filterNotNull()
            ?.map { it.toDomain() }

        if (result == null) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getMovieCredits(id: Int): Result<List<Show>> = try {
        val response = api.getMovieCredits(id)
        val result = (response.cast.orEmpty() + response.crow.orEmpty())
            .filterNotNull().map { it.toDomain(MediaType.MOVIE) }

        if (result.isEmpty()) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun getTvCredits(id: Int): Result<List<Show>> = try {
        val response = api.getTvCredits(id)
        val result = (response.cast.orEmpty() + response.crow.orEmpty())
            .filterNotNull().map { it.toDomain(MediaType.TV) }

        if (result.isEmpty()) {
            Result.failure(Exception("Something went wrong"))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}
