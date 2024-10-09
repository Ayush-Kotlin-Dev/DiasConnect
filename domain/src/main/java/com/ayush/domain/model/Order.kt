package com.ayush.domain.model

data class Order(
    val id: Long,
    val userId: Long,
    val status: OrderStatus,
    val total: Float,
    val currency: String,
    val items: List<OrderItem>,
    val createdAt: String,
    val updatedAt: String
)

data class OrderItem(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val price: Float
)

enum class OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}

data class CreateOrderInput(
    val items: List<CreateOrderItemInput>,
    val paymentMethod: String,
    val shippingAddress: String,
    val total: String
)

data class CreateOrderItemInput(
    val productId: Long,
    val quantity: Int,
    val price: String
)