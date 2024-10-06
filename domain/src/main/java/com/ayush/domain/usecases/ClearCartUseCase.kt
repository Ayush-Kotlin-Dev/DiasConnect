package com.ayush.domain.usecases

import com.ayush.domain.repository.CartRepository
import javax.inject.Inject
import com.ayush.domain.model.Result

class ClearCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartId: Long): Result<Boolean> {
        return cartRepository.clearCart(cartId)
    }
}