package com.ayush.domain.usecases

import com.ayush.domain.model.CartStatus
import com.ayush.domain.repository.CartRepository
import javax.inject.Inject
import com.ayush.domain.model.Result
class UpdateCartStatusUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartId: String, status: CartStatus): Result<Boolean> {
        return cartRepository.updateCartStatus(cartId.toLong(), status)
    }
}