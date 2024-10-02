package com.ayush.domain.usecases

import com.ayush.domain.repository.AuthRepository
import javax.inject.Inject
import com.ayush.domain.model.AuthResponse
import com.ayush.domain.model.Result

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthResponse> =
        authRepository.signIn(email, password)
}