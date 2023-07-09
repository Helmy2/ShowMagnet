package com.example.showmagnet.data.source.remote.database

import com.example.showmagnet.di.IoDispatcher
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FireStoreDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RemoteDataSource {
    companion object {
        private const val ID = "id"
    }

    override suspend fun addToFavorite(reference: CollectionReference, id: Int): Result<Boolean> =
        withContext(ioDispatcher) {
            try {
                reference.add(hashMapOf(ID to id)).await()

                Result.success(true)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }

    override suspend fun getFavoriteList(reference: CollectionReference): Result<List<Int>> =
        withContext(ioDispatcher) {
            try {
                val querySnapshot = reference.get().await()

                val list: MutableList<Int> = mutableListOf()
                for (document in querySnapshot.documents) {
                    val id = document[ID].toString().toInt()
                    id.let { list.add(it) }
                }
                Result.success(list)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }

    override suspend fun deleteFromFavorite(
        reference: CollectionReference, id: Int
    ): Result<Boolean> = withContext(ioDispatcher) {
        try {

            val personQuery = reference.whereEqualTo(ID, id).get().await()
            if (personQuery.documents.isNotEmpty()) {
                for (document in personQuery) {
                    reference.document(document.id).delete().await()
                }
            }
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun isFavorite(reference: CollectionReference, id: Int): Result<Boolean> =
        withContext(ioDispatcher) {
            try {
                val personQuery = reference.whereEqualTo(ID, id).get().await()
                Result.success(personQuery.documentChanges.isNotEmpty())
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
}