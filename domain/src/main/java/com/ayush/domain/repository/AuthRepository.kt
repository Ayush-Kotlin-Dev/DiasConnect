package com.ayush.domain.repository

import com.ayush.domain.model.AuthResponse
import com.ayush.domain.model.Result
import com.ayush.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(name: String, email: String, password: String): Result<AuthResponse>
    suspend fun signIn(email: String, password: String): Result<AuthResponse>
    suspend fun signOut()
    suspend fun getUser(): Flow<User?>
}