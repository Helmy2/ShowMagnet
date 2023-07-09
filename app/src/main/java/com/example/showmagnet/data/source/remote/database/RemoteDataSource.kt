package com.example.showmagnet.data.source.remote.database

import com.google.firebase.firestore.CollectionReference

interface RemoteDataSource {
    suspend fun addToFavorite(reference: CollectionReference, id: Int): Result<Boolean>

    suspend fun getFavoriteList(reference: CollectionReference): Result<List<Int>>

    suspend fun deleteFromFavorite(reference: CollectionReference, id: Int): Result<Boolean>

    suspend fun isFavorite(reference: CollectionReference, id: Int): Result<Boolean>
}