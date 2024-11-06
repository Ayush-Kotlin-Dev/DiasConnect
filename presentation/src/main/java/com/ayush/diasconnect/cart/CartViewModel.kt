package com.ayush.diasconnect.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.CartItem
import com.ayush.domain.model.CreateOrderInput
import com.ayush.domain.model.CreateOrderItemInput
import com.ayush.domain.model.Order
import com.ayush.domain.model.Product
import com.ayush.domain.usecases.AddItemToCartUseCase
import com.ayush.domain.usecases.ClearCartUseCase
import com.ayush.domain.usecases.CreateOrGetCartUseCase
import com.ayush.domain.usecases.CreateOrderUseCase
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
import com.ayush.domain.model.Result



data class CartUiState(
    val cartId: Long = 0,
    val items: List<CartItem> = emptyList(),
    val totalAmount: Float = 0f,
    val isLoading: Boolean = false,
    val error: String? = null,

)
sealed class CheckoutState {
    object Idle : CheckoutState()
    object CollectingInfo : CheckoutState()
    object Processing : CheckoutState()
    data class Success(val order: Order) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}
@HiltViewModel
class CartViewModel @Inject constructor(
    private val getActiveCartUseCase: GetActiveCartUseCase,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val getCartByIdUseCase: GetCartByIdUseCase,
    private val createOrGetCartUseCase: CreateOrGetCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    var showBottomSheet by mutableStateOf(false)
        private set

    init {
        loadActiveCart()
    }

    fun loadActiveCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getCartByIdUseCase().onSuccess { cart ->
                _uiState.update {
                    it.copy(
                        cartId = cart.id,
                        items = cart.items,
                        totalAmount = cart.total,
                        isLoading = false,
                        error = null
                    )
                }
            }.onError { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    fun onIncreaseQuantity(itemId: Long) = updateQuantity(itemId, 1)
    fun onDecreaseQuantity(itemId: Long) = updateQuantity(itemId, -1)

    private fun updateQuantity(itemId: Long, change: Int) {
        viewModelScope.launch {
            val item = _uiState.value.items.find { it.id == itemId } ?: return@launch
            val newQuantity = (item.quantity + change).coerceAtLeast(0)
            if (newQuantity == 0) {
                onRemoveItem(itemId)
            } else {
                updateCartItemQuantityUseCase(itemId, newQuantity)
                    .onSuccess { loadActiveCart() }
            }
        }
    }

    fun onRemoveItem(itemId: Long) {
        viewModelScope.launch {
            removeCartItemUseCase(itemId).onSuccess { loadActiveCart() }
        }
    }

    fun onCheckoutClick() {
        _checkoutState.value = CheckoutState.CollectingInfo
        showBottomSheet = true
    }

    fun onClearCart() {
        viewModelScope.launch {
            clearCartUseCase(_uiState.value.cartId).onSuccess { loadActiveCart() }
        }
    }

    fun onSubmitOrderInfo(name: String, shippingAddress: String) {
        viewModelScope.launch {
            _checkoutState.value = CheckoutState.Processing
            val input = CreateOrderInput(
                items = _uiState.value.items.map {
                    CreateOrderItemInput(
                        productId = it.productId,
                        quantity = it.quantity,
                        price = it.price.toString()
                    )
                },
                paymentMethod = "CASH_ON_DELIVERY",
                shippingAddress = shippingAddress,
                total = _uiState.value.totalAmount.toString()
            )
            createOrderUseCase(input).onSuccess { order ->
                _checkoutState.value = CheckoutState.Success(order)
                showBottomSheet = false
                onClearCart()
            }.onError { error ->
                _checkoutState.value = CheckoutState.Error(error.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetCheckoutState() {
        _checkoutState.value = CheckoutState.Idle
        showBottomSheet = false
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
        updatedAt = this.updatedAt,
        productImages = this.productImages
    )
}