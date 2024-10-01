package com.ayush.data.model


data class AuthResponse(
    val data: AuthResponseData?,
    val errorMessage: String?
)

data class AuthResponseData(
    val id: String,
    val name: String,
    val email: String,
    val token: String,
    val created: String,
    val updated: String
)

data class User(
    val id: String,
    val username: String,
    val email: String,
    val created: String,
    val updated: String
)