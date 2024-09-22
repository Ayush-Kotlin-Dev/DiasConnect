package com.ayush.diasconnect.di

import com.apollographql.apollo.ApolloClient
import com.ayush.diasconnect.data.repository.AuthRepository
import com.ayush.diasconnect.presentation.AuthViewModel
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
            .serverUrl("https://dias-connect.onrender.com/graphql")
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(apolloClient: ApolloClient): AuthRepository {
        return AuthRepository(apolloClient)
    }

    @Provides
    @Singleton
    fun provideAuthViewModel(authRepository: AuthRepository): AuthViewModel {
        return AuthViewModel(authRepository)
    }
}