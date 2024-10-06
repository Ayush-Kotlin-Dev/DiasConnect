package com.ayush.domain.usecases


import com.ayush.domain.repository.CartRepository
import javax.inject.Inject
import com.ayush.domain.model.Result
class UpdateCartItemQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItemId: Long, quantity: Int): Result<Boolean> {
        return cartRepository.updateCartItemQuantity(cartItemId, quantity)
    }
}