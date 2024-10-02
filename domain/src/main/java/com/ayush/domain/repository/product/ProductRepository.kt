package com.ayush.domain.repository.product

interface ProductRepository {

    suspend fun getProducts(): Result<List<Product>>

}