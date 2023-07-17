package com.example.showmagnet.data.source.remote.database

interface RemoteUserDataSource {
    suspend fun addToFavorite(id: Int): Result<Boolean>

    suspend fun getFavoriteList(): Result<List<Int>>

    suspend fun deleteFromFavorite(id: Int): Result<Boolean>

    suspend fun isFavorite(id: Int): Result<Boolean>
    fun setReference(type: Types)
}