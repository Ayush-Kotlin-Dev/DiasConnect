package com.ayush.domain.usecases


import com.ayush.domain.repository.CartRepository
import javax.inject.Inject

class RemoveCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItemId: String): Result<Boolean> {
        return cartRepository.removeCartItem(cartItemId.toLong())
    }
}