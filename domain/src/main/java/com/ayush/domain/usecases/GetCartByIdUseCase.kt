package com.ayush.domain.usecases


import com.ayush.domain.model.Cart
import com.ayush.domain.repository.CartRepository
import javax.inject.Inject
import com.ayush.domain.model.Result

class GetCartByIdUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartId: Long): Result<Cart> {
        return cartRepository.getCartById(cartId)
    }
}