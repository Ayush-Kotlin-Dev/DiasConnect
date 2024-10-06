package com.ayush.domain.model


data class Cart(
    val id: String,
    val userId: String,
    val status: CartStatus,
    val total: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String,
    val expiresAt: String,
    val items: List<CartItem>
)

data class CartItem(
    val id: String,
    val cartId: Long,
    val productId: String,
    val quantity: Int,
    val price: String,
    val createdAt: String,
    val updatedAt: String
)

enum class CartStatus {
    ACTIVE, COMPLETED, CANCELLED // Ensure these match your backend enum
}
//data class CartDto(
//    val id :Long,
//    val userId: Long,
//    val items: List<CartItemDto>,
//    val status: String,
//    val total: String,
//    val currency: String,
//    val createdAt: String,
//    val updatedAt: String,
//    val expiresAt: String
//) {
//    fun toCart(): Cart {
//        return Cart(
//            id = id,
//            userId = userId,
//            items = items.map { it.toCartItem() },
//            status = CartStatus.valueOf(status),
//            total = total,
//            currency = currency,
//            createdAt = createdAt,
//            updatedAt = updatedAt,
//            expiresAt = expiresAt
//
//
//        )
//    }
//}

data class CartItemDto(
    val id: String,
    val cartId: Long,
    val productId: String,
    val quantity: Int,
    val price: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toCartItem(): CartItem {
        return CartItem(
            id = id,
            cartId = cartId,
            productId = productId,
            quantity = quantity,
            price = price,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

}