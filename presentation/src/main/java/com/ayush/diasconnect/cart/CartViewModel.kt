package com.ayush.diasconnect.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// CartItem.kt
data class CartItem(
    val id: String,
    val name: String,
    val brand: String,
    val price: Double,
    val imageUrl: String,
    var quantity: Int = 0
)

// CartUiState.kt
data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalAmount: Double = 0.0
)

class CartViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val dummyItems = listOf(
        CartItem(
            id = "1",
            name = "Watch",
            brand = "Rolex",
            price = 40.0,
            imageUrl = "https://avatars.githubusercontent.com/u/11629653?v=4",
            quantity = 2
        ),
        CartItem(
            id = "2",
            name = "Airpods",
            brand = "Apple",
            price = 333.0,
            imageUrl = "https://avatars.githubusercontent.com/u/11629653?v=4",
            quantity = 2
        ),
        CartItem(
            id = "3",
            name = "Hoodie",
            brand = "Puma",
            price = 50.0,
            imageUrl = "https://avatars.githubusercontent.com/u/11629653?v=4",
            quantity = 2
        )
    )

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // TODO: Replace with actual API call
                delay(500) // Simulate network delay
                _uiState.value = _uiState.value.copy(
                    items = dummyItems,
                    isLoading = false,
                    totalAmount = calculateTotal(dummyItems)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun calculateTotal(items: List<CartItem>): Double {
        return items.sumOf { it.price * it.quantity }
    }

    fun onIncreaseQuantity(itemId: String) {
        // TODO: Implement increase quantity logic
        val updatedItems = _uiState.value.items.map { item ->
            if (item.id == itemId) {
                item.copy(quantity = item.quantity + 1)
            } else {
                item
            }
        }
        updateItems(updatedItems)
    }

    fun onDecreaseQuantity(itemId: String) {
        // TODO: Implement decrease quantity logic
        val updatedItems = _uiState.value.items.map { item ->
            if (item.id == itemId && item.quantity > 0) {
                item.copy(quantity = item.quantity - 1)
            } else {
                item
            }
        }
        updateItems(updatedItems)
    }

    fun onRemoveItem(itemId: String) {
        // TODO: Implement remove item logic
        val updatedItems = _uiState.value.items.filter { it.id != itemId }
        updateItems(updatedItems)
    }

    fun onCheckoutClick() {
        // TODO: Implement checkout logic
    }

    private fun updateItems(items: List<CartItem>) {
        _uiState.value = _uiState.value.copy(
            items = items,
            totalAmount = calculateTotal(items)
        )
    }
}