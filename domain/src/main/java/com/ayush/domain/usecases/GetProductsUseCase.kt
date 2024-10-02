package com.ayush.domain.usecases

import com.ayush.domain.model.Product
import com.ayush.domain.repository.ProductRepository
import javax.inject.Inject
import com.ayush.domain.model.Result
class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>> =
        productRepository.getProducts()
}