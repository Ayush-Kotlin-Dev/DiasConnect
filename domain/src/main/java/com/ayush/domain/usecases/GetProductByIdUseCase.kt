package com.ayush.domain.usecases

import com.ayush.domain.model.Product
import com.ayush.domain.repository.ProductRepository
import javax.inject.Inject
import com.ayush.domain.model.Result

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String): Result<Product> {
        return productRepository.getProductById(productId)
    }
}