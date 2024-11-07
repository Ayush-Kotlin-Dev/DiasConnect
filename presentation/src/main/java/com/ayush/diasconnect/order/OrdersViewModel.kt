package com.ayush.diasconnect.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.data.datastore.UserPreferences
import com.ayush.domain.model.Order
import com.ayush.domain.model.OrderStatus
import com.ayush.domain.model.myOrder
import com.ayush.domain.usecases.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
)  : ViewModel() {
    private val _orders = MutableStateFlow<List<myOrder>>(emptyList())
    val orders: StateFlow<List<myOrder>> = _orders.asStateFlow()

    init {

        fetchOrders()
    }

    private fun fetchOrders() {
        viewModelScope.launch {
            getOrdersUseCase().onSuccess { orders ->
                _orders.value = orders
            }.onError {
                // Handle error
                emptyList<myOrder>()
            }

        }

    }
}