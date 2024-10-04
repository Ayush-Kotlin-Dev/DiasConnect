package com.ayush.diasconnect.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.domain.model.CartItem
import com.ayush.diasconnect.cart.CartViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color


// CartScreen.kt
@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CartHeader(onNavigateBack)
        CartItems(
            items = uiState.items,
            onIncreaseQuantity = viewModel::onIncreaseQuantity,
            onDecreaseQuantity = viewModel::onDecreaseQuantity,
            onRemoveItem = viewModel::onRemoveItem
        )
        Spacer(modifier = Modifier.weight(1f))
        CheckoutButton(
            totalAmount = uiState.totalAmount,
            onClick = viewModel::onCheckoutClick
        )
    }
}

@Composable
private fun CartHeader(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = "Cart",
            style = MaterialTheme.typography.labelMedium
        )
        IconButton(onClick = { /* TODO: Menu options */ }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
    }
}

@Composable
private fun CartItems(
    items: List<CartItem>,
    onIncreaseQuantity: (String) -> Unit,
    onDecreaseQuantity: (String) -> Unit,
    onRemoveItem: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            CartItemRow(
                item = item,
                onIncreaseQuantity = { onIncreaseQuantity(item.id) },
                onDecreaseQuantity = { onDecreaseQuantity(item.id) },
                onRemoveItem = { onRemoveItem(item.id) }
            )
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemoveItem: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = item.name, style = MaterialTheme.typography.subtitle1)
                Text(text = item.brand, style = MaterialTheme.typography.caption)
                Text(
                    text = "Rs ${item.price}",
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.primary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onDecreaseQuantity,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Decrease quantity"
                    )
                }

                Text(
                    text = "${item.quantity}",
                    style = MaterialTheme.typography.body1
                )

                IconButton(
                    onClick = onIncreaseQuantity,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Increase quantity"
                    )
                }

                IconButton(
                    onClick = onRemoveItem,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove item",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckoutButton(
    totalAmount: Double,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = "Checkout - $${String.format("%.2f", totalAmount)}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}