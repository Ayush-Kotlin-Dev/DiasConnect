package com.ayush.domain.repository

import com.ayush.domain.model.Product
import com.ayush.domain.model.Result

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductsByCategory(categoryId: Long): Result<List<Product>>
    suspend fun getProductById(productId: Long): Result<Product>
    suspend fun searchProducts(query: String): Result<List<Product>>

}