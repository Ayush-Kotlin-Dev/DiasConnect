package com.ayush.diasconnect.detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.CartItem
import com.ayush.domain.model.Product
import com.ayush.domain.model.Result
import com.ayush.domain.usecases.AddItemToCartUseCase
import com.ayush.domain.usecases.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Initial)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: Long) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            getProductByIdUseCase(productId)
                .onSuccess { product ->
                    _uiState.value = ProductDetailUiState.Success(product)
                }
                .onError { error ->
                    _uiState.value = ProductDetailUiState.Error(error.message ?: "An unknown error occurred")
                }
        }
    }

    fun addItemToCart(productId: Long, price: Double, quantity: Int = 1) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                if (currentState is ProductDetailUiState.Success) {
                    currentState.copy(isAddingToCart = true)
                } else currentState
            }

            addItemToCartUseCase(productId, quantity, price)
                .onSuccess { cartItemId ->
                    _uiState.update { currentState ->
                        if (currentState is ProductDetailUiState.Success) {
                            currentState.copy(isAddingToCart = false, addToCartSuccess = true)
                        } else currentState
                    }
                }
                .onError { error ->
                    _uiState.update { currentState ->
                        if (currentState is ProductDetailUiState.Success) {
                            currentState.copy(isAddingToCart = false, error = error.message ?: "Failed to add to cart")
                        } else currentState
                    }
                }
        }
    }
}

sealed class ProductDetailUiState {
    object Initial : ProductDetailUiState()
    object Loading : ProductDetailUiState()
    data class Success(
        val product: Product,
        val isAddingToCart: Boolean = false,
        val addToCartSuccess: Boolean = false,
        val error: String? = null
    ) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}