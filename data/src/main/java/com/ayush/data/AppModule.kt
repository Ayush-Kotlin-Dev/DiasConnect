package com.ayush.data

import com.apollographql.apollo.ApolloClient
import com.ayush.data.repository.AuthRepository
import com.ayush.data.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://diasconnect-buyer-backend.onrender.com/graphql")
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(apolloClient: ApolloClient): AuthRepository {
        return AuthRepository(apolloClient)
    }

    @Provides
    @Singleton
    fun provideProductRepository(apolloClient: ApolloClient): ProductRepositoryImpl {
        return ProductRepositoryImpl(apolloClient)
    }

}