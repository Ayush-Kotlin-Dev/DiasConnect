package com.ayush.domain.usecases

import com.ayush.domain.model.Cart
import com.ayush.domain.repository.CartRepository
import javax.inject.Inject


class GetActiveCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(userId: String): Result<Cart> {
        return cartRepository.getActiveCartByUserId(userId.toLong())
    }
}