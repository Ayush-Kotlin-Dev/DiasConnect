package com.ayush.domain.model


data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val images: List<String>,
    val categoryId: Long,
    val sellerId: Long,
    val createdAt: String,
    val updatedAt: String
)