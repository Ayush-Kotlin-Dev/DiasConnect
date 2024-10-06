package com.ayush.data.repository

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.ayush.data.GetCartByIdQuery

import com.ayush.domain.model.Cart
import com.ayush.domain.model.CartItem
import com.ayush.domain.model.CartStatus
import com.ayush.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
): CartRepository {
    override suspend fun getActiveCartByUserId(userId: Long): Result<Cart> {
        TODO("Not yet implemented")
    }

    override suspend fun getCartById(cartId: Long): Result<Cart> {
        return try {
            val response = apolloClient.query(GetCartByIdQuery(cartId)).execute()
            val cartData = response.data?.getCartById
            Log.d("CartRepositoryImpl", "Cart data: $cartData")
            if (cartData != null) {
                val cart = Cart(
                    id = cartData.id,
                    userId = cartData.userId,
                    items = cartData.items.map { item ->
                        CartItem(
                            id = item.id,
                            cartId = cartId,
                            productId = item.productId,
                            quantity = item.quantity,
                            price = item.price,
                            createdAt = "",
                            updatedAt = ""
                        )
                    },
                    status = CartStatus.valueOf(cartData.status.name),
                    total = 0.0.toString(), // Assuming you have a way to calculate the total
                    currency = "", // Assuming you have a way to get the currency
                    createdAt = "", // Assuming you have a way to get these values
                    updatedAt = "", // Assuming you have a way to get these values
                    expiresAt = ""
                )
                Log.d("CartRepositoryImpl", "Cart: $cart")
                Result.success(cart)
            } else {
                Log.d("CartRepositoryImpl", " Error Cart not found")
                Result.failure(Exception("Cart not found"))
            }
        } catch (e: Exception) {
            Log.e("CartRepositoryImpl", "Error: $e")
            Result.failure(e)
        }
    }

    override suspend fun createOrGetCart(userId: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addItemToCart(
        cartId: String,
        productId: String,
        quantity: Int,
        price: String
    ): Result<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun removeCartItem(cartItemId: Long): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun clearCart(cartId: Long): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCartStatus(cartId: Long, status: CartStatus): Result<Boolean> {
        TODO("Not yet implemented")
    }
}