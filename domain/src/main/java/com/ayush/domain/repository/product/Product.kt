package com.ayush.domain.repository.product


data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val images: List<String>,
    val categoryId: String,
    val sellerId: String,
    val createdAt: String,
    val updatedAt: String
)