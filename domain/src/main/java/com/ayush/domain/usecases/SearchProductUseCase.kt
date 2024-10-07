package com.ayush.domain.usecases

import com.ayush.domain.model.Product
import com.ayush.domain.model.Result
import com.ayush.domain.repository.ProductRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(query: String): Result<List<Product>> {
        return productRepository.searchProducts(query)
    }
}