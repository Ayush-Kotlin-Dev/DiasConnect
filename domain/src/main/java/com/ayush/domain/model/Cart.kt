package com.ayush.domain.model


data class Cart(
    val id: Long,
    val userId: Long,
    val status: CartStatus,
    val total: Float,
    val currency: String,
    val createdAt: String  ,
    val updatedAt: String,
    val expiresAt: String,
    val items: List<CartItem>
)

data class CartItem(
    val id: Long,
    val cartId: Long,
    val productId: Long,
    val quantity: Int,
    val price: Float,
    val productName: String,
    val productDescription: String,
    val productImages : List<String>,
    val createdAt: String,
    val updatedAt: String
)

enum class CartStatus {
    ACTIVE, COMPLETED, CANCELLED
}