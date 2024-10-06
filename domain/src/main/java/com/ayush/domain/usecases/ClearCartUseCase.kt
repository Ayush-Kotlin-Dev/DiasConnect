package com.ayush.domain.usecases

import com.ayush.domain.repository.CartRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartId: String): Result<Boolean> {
        return cartRepository.clearCart(cartId.toLong())
    }
}