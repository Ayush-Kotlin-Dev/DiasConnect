package com.ayush.domain.usecases

import com.ayush.domain.repository.CartRepository
import javax.inject.Inject

class CreateOrGetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(userId: String): Result<String> {
        return cartRepository.createOrGetCart(userId)
    }
}