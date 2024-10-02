package com.ayush.domain.repository

import com.ayush.domain.model.Product
import com.ayush.domain.model.Result

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductsByCategory(categoryId: String): Result<List<Product>>
}