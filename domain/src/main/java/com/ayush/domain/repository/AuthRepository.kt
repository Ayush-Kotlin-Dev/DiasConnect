package com.ayush.domain.repository

import com.ayush.domain.model.AuthResponse
import com.ayush.domain.model.Result

interface AuthRepository {
    suspend fun signUp(name: String, email: String, password: String): Result<AuthResponse>

    suspend fun signIn(email: String, password: String): Result<AuthResponse>


}