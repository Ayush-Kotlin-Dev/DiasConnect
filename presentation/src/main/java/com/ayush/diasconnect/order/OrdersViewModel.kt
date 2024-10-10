package com.ayush.diasconnect.order

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@ExperimentalStdlibApi
@HiltViewModel
class OrdersViewModel  : ViewModel() {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    init {

        fetchOrders()
    }

    private fun fetchOrders() {
//         some dummy data
        val dummyOrders = listOf(
            Order("1", listOf("Item 1", "Item 2"), 25.99, "Delivered"),
            Order("2", listOf("Item 3"), 15.50, "Processing"),
            Order("3", listOf("Item 4", "Item 5", "Item 6"), 45.00, "Shipped")
        )
        _orders.value = dummyOrders
    }
}