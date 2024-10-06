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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Initial)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _addToCartState = MutableStateFlow<AddToCartState>(AddToCartState.Initial)
    val addToCartState: StateFlow<AddToCartState> = _addToCartState.asStateFlow()

    fun loadProduct(productId: Long) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            when (val result = getProductByIdUseCase(productId)) {
                is Result.Success -> {
                    _uiState.value = ProductDetailUiState.Success(result.data)
                }

                is Result.Error -> {
                    _uiState.value = ProductDetailUiState.Error(
                        result.exception.message ?: "An unknown error occurred"
                    )
                }

                Result.Loading -> {
                    // Do nothing
                }
            }
        }
    }

    fun addItemToCart(productId: Long, quantity: Int = 1) {
        viewModelScope.launch {
            _addToCartState.value = AddToCartState.Loading
            val userId = 1L // Hardcoded for now
            when (val result = addItemToCartUseCase(userId, productId, quantity, 69.6)) {
                is Result.Success -> {
                    _addToCartState.value = AddToCartState.Success(result.data)
                }

                is Result.Error -> {
                    _addToCartState.value = AddToCartState.Error(
                        result.exception.message ?: "An unknown error occurred"
                    )
                }

                Result.Loading -> {
                    // Do nothing, already handled
                }
            }
        }
    }
}

sealed class ProductDetailUiState {
    object Initial : ProductDetailUiState()
    object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}

sealed class AddToCartState {
    object Initial : AddToCartState()
    object Loading : AddToCartState()
    data class Success(val cartItem: Long) : AddToCartState()
    data class Error(val message: String) : AddToCartState()
}