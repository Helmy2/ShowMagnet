package com.example.showmagnet.domain.repository

import com.example.showmagnet.domain.model.SignResult

interface UserRepository {
    suspend fun updateProfileName(name: String): SignResult

    suspend fun signOut(): Boolean
}