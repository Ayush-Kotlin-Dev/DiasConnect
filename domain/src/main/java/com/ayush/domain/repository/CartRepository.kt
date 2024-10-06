package com.ayush.domain.repository

import com.ayush.domain.model.Cart
import com.ayush.domain.model.CartStatus

interface CartRepository {
    suspend fun getActiveCartByUserId(userId: Long): Result<Cart>
    suspend fun getCartById(cartId: Long): Result<Cart>
    suspend fun createOrGetCart(userId: Long): Result<String>
    suspend fun addItemToCart(cartId: String, productId: String, quantity: Int, price: String): Result<Long>
    suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): Result<Boolean>
    suspend fun removeCartItem(cartItemId: Long): Result<Boolean>
    suspend fun clearCart(cartId: Long): Result<Boolean>
    suspend fun updateCartStatus(cartId: Long, status: CartStatus): Result<Boolean>
}