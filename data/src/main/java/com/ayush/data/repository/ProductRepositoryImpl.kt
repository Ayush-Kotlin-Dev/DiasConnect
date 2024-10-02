package com.ayush.data.repository

import com.apollographql.apollo.ApolloClient
import com.ayush.GetProductsQuery
import com.ayush.domain.model.Product
import com.ayush.domain.repository.ProductRepository
import javax.inject.Inject
import com.ayush.domain.model.Result

//TODO  apollo coroutines
class ProductRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : ProductRepository {
    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = apolloClient.query(GetProductsQuery()).execute()
            when {
                response.hasErrors() -> {
                    Result.error(Exception(response.errors?.first()?.message))
                }
                else -> {
                    val products = response.data?.products?.map { it.toProduct() } ?: emptyList()
                    Result.success(products)
                }
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    private fun GetProductsQuery.Product.toProduct(): Product {
        return Product(
            id = this.id,
            name = this.name,
            description = this.description,
            price = this.price,
            stock = this.stock,
            images = this.images,
            categoryId = this.categoryId,
            sellerId = this.sellerId,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}