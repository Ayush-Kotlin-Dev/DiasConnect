package com.ayush.domain.usecases

import com.ayush.domain.repository.AuthRepository
import javax.inject.Inject
import com.ayush.domain.model.AuthResponse
import com.ayush.domain.model.Result

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<AuthResponse> =
        authRepository.signUp(name, email, password)
}