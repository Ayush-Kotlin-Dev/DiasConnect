package com.ayush.diasconnect.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.CartItem
import com.ayush.domain.model.Product
import com.ayush.domain.usecases.AddItemToCartUseCase
import com.ayush.domain.usecases.ClearCartUseCase
import com.ayush.domain.usecases.CreateOrGetCartUseCase
import com.ayush.domain.usecases.GetActiveCartUseCase
import com.ayush.domain.usecases.GetCartByIdUseCase
import com.ayush.domain.usecases.RemoveCartItemUseCase
import com.ayush.domain.usecases.UpdateCartItemQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



data class CartUiState(
    val cartId: Long = 0,
    val items: List<CartItem> = emptyList(),
    val totalAmount: Float = 0f,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getActiveCartUseCase: GetActiveCartUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val getCartByIdUseCase: GetCartByIdUseCase,
    private val createOrGetCartUseCase: CreateOrGetCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private var currentUserId: Long = 1

    init {
        viewModelScope.launch {
            loadActiveCart()
        }
    }
    private fun createCart() {
        viewModelScope.launch {
            createOrGetCartUseCase(currentUserId).onSuccess { cartId ->
                Log.d("CartViewModel", "Cart created: $cartId")
            }.onFailure {
                Log.d("CartViewModel", "Error: $it")
            }

        }
    }
    private suspend fun loadActiveCart() {
        _uiState.update { it.copy(isLoading = true) }
        getCartByIdUseCase(1).onSuccess { cart ->
            Log.d("CartViewModel", "Cart: $cart")
            _uiState.update {
                it.copy(
                    cartId = cart.id,
                    items = cart.items.map { domainItem -> domainItem.toCartItem() },
                    totalAmount = cart.total,
                    isLoading = false,
                    error = null
                )
            }
        }.onFailure { error ->
            Log.d("CartViewModel", "Error: $error")
            _uiState.update { it.copy(isLoading = false, error = error.message) }
        }
    }

    fun onIncreaseQuantity(itemId: Long) {
        viewModelScope.launch {
            val item = _uiState.value.items.find { it.id == itemId } ?: return@launch
            updateCartItemQuantityUseCase(itemId, item.quantity + 1).onSuccess {
                loadActiveCart()
            }
        }
    }

    fun onDecreaseQuantity(itemId: Long) {
        viewModelScope.launch {
            val item = _uiState.value.items.find { it.id == itemId } ?: return@launch
            if (item.quantity > 1) {
                updateCartItemQuantityUseCase(itemId, item.quantity - 1).onSuccess {
                    loadActiveCart()
                }
            } else {
                onRemoveItem(itemId)
            }
        }
    }

    fun onRemoveItem(itemId: Long) {
        viewModelScope.launch {
            removeCartItemUseCase(itemId).onSuccess {
                loadActiveCart()
            }
        }
    }

    fun onCheckoutClick() {
        // Implement checkout logic
    }

    fun onClearCart() {
        viewModelScope.launch {
            clearCartUseCase(_uiState.value.cartId).onSuccess {
                loadActiveCart()
            }
        }
    }
}

private fun CartItem.toCartItem(): CartItem {
    return CartItem(
        id = this.id,
        cartId = this.cartId,
        productId = this.productId,
        quantity = this.quantity,
        price = this.price,
        productName = this.productName,
        productDescription = this.productDescription,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}