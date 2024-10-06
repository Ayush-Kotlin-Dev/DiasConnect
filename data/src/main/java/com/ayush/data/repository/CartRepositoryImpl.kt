package com.ayush.data.repository

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.ayush.data.GetCartByIdQuery
import com.ayush.data.GetProductByIdQuery
import com.ayush.domain.model.Cart
import com.ayush.domain.model.CartItem
import com.ayush.domain.model.CartStatus
import com.ayush.domain.model.Product
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

            if (cartData != null) {
                val cartItems = cartData.items.map { item ->
                    CartItem(
                        id = item.id,
                        cartId = cartId,
                        productId = item.productId,
                        quantity = item.quantity,
                        price = item.price.toFloat(),
                        productName = item.productName,
                        productDescription = item.productDescription,
                        createdAt = item.createdAt,
                        updatedAt = item.updatedAt
                    )
                }

                val cart = Cart(
                    id = cartData.id,
                    userId = cartData.userId,
                    items = cartItems,
                    status = CartStatus.valueOf(cartData.status.name),
                    total = cartData.total.toFloat(),
                    currency = cartData.currency,
                    createdAt = cartData.createdAt,
                    updatedAt = cartData.updatedAt,
                    expiresAt = cartData.expiresAt
                )
                Result.success(cart)
            } else {
                Result.failure(Exception("Cart not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }



    }

    override suspend fun createOrGetCart(userId: Long): Result<String> {
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