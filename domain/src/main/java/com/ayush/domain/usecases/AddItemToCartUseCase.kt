package com.ayush.domain.usecases

import com.ayush.domain.repository.CartRepository
import javax.inject.Inject

class AddItemToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
//    suspend operator fun invoke(cartId: String, productId: String, quantity: Int, price: Double): Result<String> {
//        return cartRepository.addItemToCart(cartId, productId, quantity, price)
//    }
}