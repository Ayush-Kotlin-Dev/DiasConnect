package com.ayush.domain.usecases

import com.ayush.domain.repository.CartRepository
import javax.inject.Inject
import com.ayush.domain.model.Result

class AddItemToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: Long, quantity: Int, price: Double): Result<Long> {
        return cartRepository.addItemToCart( productId, quantity, price.toString())
    }
}