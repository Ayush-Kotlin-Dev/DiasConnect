package com.ayush.diasconnect.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import dagger.hilt.android.lifecycle.HiltViewModel

data class Order(val id: String, val items: List<String>, val totalAmount: Double, val status: String)

class OrdersScreen : Screen {
    @OptIn(ExperimentalStdlibApi::class)
    @Composable
    override fun Content() {
        val viewModel : OrdersViewModel = hiltViewModel()
        val orders by viewModel.orders.collectAsState()

        Scaffold(
            topBar = {

            }
        ) { paddingValues ->
            OrdersList(orders = orders, modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun OrdersList(orders: List<Order>, modifier: Modifier = Modifier) {
    if (orders.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No orders found")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(orders) { order ->
                OrderCard(order)
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Order #${order.id}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Items: ${order.items.joinToString(", ")}")
            Text(text = "Total: $${order.totalAmount}")
            Text(text = "Status: ${order.status}")
        }
    }
}

