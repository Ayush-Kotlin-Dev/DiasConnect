package com.ayush.diasconnect.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.Product
import com.ayush.domain.usecases.GetProductsByCategoryUseCase
import com.ayush.domain.usecases.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.LoadProducts -> loadProducts()
            is ProductEvent.LoadProductsByCategory -> loadProductsByCategory(event.categoryId)
            is ProductEvent.Search -> searchProducts(event.query)
            ProductEvent.ClearError -> clearError()
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getProductsUseCase()
                .onSuccess { products ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        allProducts = products,
                        featuredProducts = products.take(5),
                        popularProducts = products.takeLast(5),
                        selectedCategoryId = null
                    ) }
                }
                .onError { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = error.message
                    ) }
                }
        }
    }

    private fun loadProductsByCategory(categoryId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getProductsByCategoryUseCase(categoryId)
                .onSuccess { products ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        allProducts = products,
                        selectedCategoryId = categoryId
                    ) }
                }
                .onError { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = error.message
                    ) }
                }
        }
    }

    private fun searchProducts(query: String) {
        val filteredProducts = _uiState.value.allProducts.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
        _uiState.update { it.copy(searchResults = filteredProducts) }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class ProductsUiState(
    val isLoading: Boolean = false,
    val allProducts: List<Product> = emptyList(),
    val featuredProducts: List<Product> = emptyList(),
    val popularProducts: List<Product> = emptyList(),
    val searchResults: List<Product> = emptyList(),
    val error: String? = null,
    val selectedCategoryId: String? = null
)

sealed class ProductEvent {
    object LoadProducts : ProductEvent()
    data class LoadProductsByCategory(val categoryId: String) : ProductEvent()
    data class Search(val query: String) : ProductEvent()
    object ClearError : ProductEvent()
}