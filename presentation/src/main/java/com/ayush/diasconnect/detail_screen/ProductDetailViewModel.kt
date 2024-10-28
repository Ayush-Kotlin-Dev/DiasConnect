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
    private val addItemToCartUseCase: AddItemToCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Initial)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _addToCartState = MutableStateFlow<AddToCartState>(AddToCartState.Initial)
    val addToCartState: StateFlow<AddToCartState> = _addToCartState.asStateFlow()

    fun loadProduct(productId: Long) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            getProductByIdUseCase(productId).fold(
                onSuccess = { product ->
                    _uiState.value = ProductDetailUiState.Success(product)
                },
                onFailure = { error ->
                    _uiState.value = ProductDetailUiState.Error(error.message ?: "An unknown error occurred")
                }
            )
        }
    }

    fun addItemToCart(productId: Long, price: Double, quantity: Int = 1) {
        viewModelScope.launch {
            _addToCartState.value = AddToCartState.Loading
            addItemToCartUseCase(productId, quantity, price).fold(
                onSuccess = { cartItemId ->
                    _addToCartState.value = AddToCartState.Success(cartItemId)
                },
                onFailure = { error ->
                    _addToCartState.value = AddToCartState.Error(error.message ?: "An unknown error occurred")
                }
            )
        }
    }

    fun resetAddToCartState() {
        _addToCartState.value = AddToCartState.Initial
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
    data class Success(val cartItemId: Long) : AddToCartState()
    data class Error(val message: String) : AddToCartState()
}