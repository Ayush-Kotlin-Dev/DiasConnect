package com.ayush.data.repository

import com.apollographql.apollo.ApolloClient
import com.ayush.SignInMutation
import com.ayush.SignUpMutation
import com.ayush.domain.model.AuthResponse
import com.ayush.domain.model.AuthResponseData
import com.ayush.domain.model.Result
import com.ayush.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : AuthRepository {
    override suspend fun signUp(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apolloClient.mutation(
                SignUpMutation(name = name, email = email, password = password)
            ).execute()

            when {
                response.hasErrors() -> {
                    Result.error(
                        Exception(
                            response.errors?.firstOrNull()?.message ?: "Unknown error"
                        )
                    )
                }

                response.data?.signUp?.data != null -> {
                    Result.success(
                        AuthResponse(
                            data = response.data?.signUp?.data?.let {
                                AuthResponseData(
                                    id = it.id,
                                    name = it.name,
                                    email = it.email,
                                    token = it.token,
                                    created = it.created,
                                    updated = it.updated
                                )
                            },
                            errorMessage = null
                        )
                    )
                }

                else -> Result.error(
                    Exception(
                        response.data?.signUp?.errorMessage ?: "Unknown error"
                    )
                )
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apolloClient.mutation(
                SignInMutation(email = email, password = password)
            ).execute()

            when {
                response.hasErrors() -> {
                    Result.error(
                        Exception(
                            response.errors?.firstOrNull()?.message ?: "Unknown error"
                        )
                    )
                }

                response.data?.signIn?.data != null -> {
                    Result.success(
                        AuthResponse(
                            data = response.data?.signIn?.data?.let {
                                AuthResponseData(
                                    id = it.id,
                                    name = it.name, email = it.email,
                                    token = it.token,
                                    created = it.created,
                                    updated = it.updated
                                )
                            },
                            errorMessage = null
                        )
                    )
                }

                else -> Result.error(
                    Exception(
                        response.data?.signIn?.errorMessage ?: "Unknown error"
                    )
                )
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }
}
