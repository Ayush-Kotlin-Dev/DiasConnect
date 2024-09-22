package com.ayush.diasconnect.data.repository

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.exception.ApolloException
import com.ayush.diasconnect.SignInMutation
import com.ayush.diasconnect.SignUpMutation
import com.ayush.diasconnect.model.AuthResponse
import com.ayush.diasconnect.model.AuthResponseData
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun signUp(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apolloClient.mutation(SignUpMutation(name = name, email = email, password = password)).execute()
            handleSignUpResponse(response)
        } catch (e: ApolloException) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apolloClient.mutation(SignInMutation(email = email, password = password)).execute()
            handleSignInResponse(response)
        } catch (e: ApolloException) {
            Result.failure(e)
        }
    }

    private fun handleSignUpResponse(response: ApolloResponse<SignUpMutation.Data>): Result<AuthResponse> {
        val signUpData = response.data?.signUp
        return if (response.errors?.isNotEmpty() == true) {
            Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error occurred"))
        } else {
            Result.success(
                AuthResponse(
                    data = signUpData?.data?.let {
                        AuthResponseData(
                            id = it.id,
                            name = it.name,
                            email = it.email,
                            token = it.token,
                            created = it.created,
                            updated = it.updated
                        )
                    },
                    errorMessage = signUpData?.errorMessage
                )
            )
        }
    }

    private fun handleSignInResponse(response: ApolloResponse<SignInMutation.Data>): Result<AuthResponse> {
        val signInData = response.data?.signIn
        return if (response.errors?.isNotEmpty() == true) {
            Result.failure(Exception(response.errors?.firstOrNull()?.message ?: "Unknown error occurred"))
        } else {
            Result.success(
                AuthResponse(
                    data = signInData?.data?.let {
                        AuthResponseData(
                            id = it.id,
                            name = it.name,
                            email = it.email,
                            token = it.token,
                            created = it.created,
                            updated = it.updated
                        )
                    },
                    errorMessage = signInData?.errorMessage
                )
            )
        }
    }
}