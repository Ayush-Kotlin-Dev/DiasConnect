package com.ayush.domain.module

import com.ayush.domain.repository.ProductRepository
import com.ayush.domain.usecases.GetProductByIdUseCase
import com.ayush.domain.usecases.GetProductsByCategoryUseCase
import com.ayush.domain.usecases.GetProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetProductByIdUseCase(productRepository: ProductRepository): GetProductByIdUseCase {
        return GetProductByIdUseCase(productRepository)
    }

    @Provides
    @Singleton
    fun provideGetProductsUseCase(productRepository: ProductRepository): GetProductsUseCase {
        return GetProductsUseCase(productRepository)
    }

    @Provides
    @Singleton
    fun provideGetProductsByCategoryUseCase(productRepository: ProductRepository): GetProductsByCategoryUseCase {
        return GetProductsByCategoryUseCase(productRepository)
    }

    // Add more use case providers as needed
}