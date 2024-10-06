package com.ayush.domain.usecases


import com.ayush.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItemId: String, quantity: Int): Result<Boolean> {
        return cartRepository.updateCartItemQuantity(cartItemId.toLong(), quantity)
    }
}