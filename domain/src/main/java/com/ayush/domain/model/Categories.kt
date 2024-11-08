package com.ayush.domain.model


data class Category(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val subCategories: List<SubCategory> = emptyList()
)

data class SubCategory(
    val id: Long,
    val name: String,
    val imageUrl: String
)