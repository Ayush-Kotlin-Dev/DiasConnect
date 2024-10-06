package com.ayush.domain.usecases


import com.ayush.domain.repository.CartRepository
import javax.inject.Inject
import com.ayush.domain.model.Result

class RemoveCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItemId: Long): Result<Boolean> {
        return cartRepository.removeCartItem(cartItemId)
    }
}