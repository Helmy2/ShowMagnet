package com.example.showmagnet.data.source.remote.database

import com.example.showmagnet.di.CurrentFirebaseUser
import com.example.showmagnet.di.IoDispatcher
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FireStoreUserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @CurrentFirebaseUser private val user: FirebaseUser?,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RemoteUserDataSource {

    private var reference: CollectionReference? = null

    override fun setReference(type: Types) {
        val userId = user?.uid ?: throw Exception("User not found")

        reference = firestore.collection(USERS).document(userId).collection(type.value)
    }

    companion object {
        private const val ID = "id"
        private const val USERS = "users"
    }

    override suspend fun addToFavorite(id: Int): Result<Boolean> =
        withContext(ioDispatcher) {
            if (reference == null)
                throw Exception("Reference is null")
            try {
                reference!!.add(hashMapOf(ID to id)).await()

                Result.success(true)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }

    override suspend fun getFavoriteList(): Result<List<Int>> =
        withContext(ioDispatcher) {
            if (reference == null)
                throw Exception("Reference is null")
            try {
                val querySnapshot = reference!!.get().await()

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
        id: Int
    ): Result<Boolean> = withContext(ioDispatcher) {
        if (reference == null)
            throw Exception("Reference is null")
        try {
            val personQuery = reference!!.whereEqualTo(ID, id).get().await()
            if (personQuery.documents.isNotEmpty()) {
                for (document in personQuery) {
                    reference!!.document(document.id).delete().await()
                }
            }
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun isFavorite(id: Int): Result<Boolean> =
        withContext(ioDispatcher) {
            if (reference == null)
                throw Exception("Reference is null")
            try {
                val personQuery = reference!!.whereEqualTo(ID, id).get().await()
                Result.success(personQuery.documentChanges.isNotEmpty())
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
}