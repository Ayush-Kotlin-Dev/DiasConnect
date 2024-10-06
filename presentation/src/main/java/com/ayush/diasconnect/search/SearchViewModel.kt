package com.ayush.diasconnect.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val dummyProducts = listOf(
        Product(id = 1, name = "Smartphone", price = 599.99, stock = 10, description = "A very nice smartphone", images = emptyList(), categoryId = 1, sellerId = 1, createdAt = "", updatedAt = ""),
        Product(id = 2, name = "Laptop", price = 1299.99, stock = 5, description = "A very nice laptop", images = emptyList(), categoryId = 2, sellerId = 1, createdAt = "", updatedAt = ""),
        Product(id = 3, name = "Tablet", price = 399.99, stock = 3, description = "A very nice tablet", images = emptyList(), categoryId = 3, sellerId = 1, createdAt = "", updatedAt = ""),
        Product(id = 4, name = "Smartwatch", price = 199.99, stock = 7, description = "A very nice smartwatch", images = emptyList(), categoryId = 4, sellerId = 1, createdAt = "", updatedAt = ""),
        Product(id = 5, name = "Headphones", price = 99.99, stock = 15, description = "A very nice headphones", images = emptyList(), categoryId = 5, sellerId = 1, createdAt = "", updatedAt = "")

    )

    fun onSearchQueryChange(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(searchQuery = query) }
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        val results = if (query.isBlank()) {
            emptyList()
        } else {
            dummyProducts.filter { it.name.contains(query, ignoreCase = true) }
        }
        _uiState.update { it.copy(searchResults = results) }
    }
}

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList()
)