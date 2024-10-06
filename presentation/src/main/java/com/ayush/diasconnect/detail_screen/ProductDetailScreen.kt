package com.ayush.diasconnect.detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.ayush.domain.model.Product

data class ProductDetailScreen(val productId: Long) : Screen {
    @Composable
    override fun Content() {
        val viewModel: ProductDetailViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(productId) {
            viewModel.loadProduct(productId)
        }

        ProductDetailContent(uiState) { productId ->
            viewModel.addItemToCart(productId)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailContent(
    uiState: ProductDetailUiState,
    onBackClick: (Long) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        bottomBar = {
            when (uiState) {
                is ProductDetailUiState.Success -> {
                    BottomBar(uiState.product) {
                        onBackClick(uiState.product.id)
                    }
                }
                else -> {}
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is ProductDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProductDetailUiState.Success -> {
                    ProductDetails(uiState.product) {
                        navigator.pop()
                    }
                }
                is ProductDetailUiState.Error -> {
                    ErrorMessage(uiState.message)
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun ProductDetails(product: Product, onBackClick: () -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) Color.Red else Color.Black
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFB800),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "4.5",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(
                    text = "(20 Review)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Size Section
            Text(
                text = "Size",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Size Options
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(listOf("8", "10", "38", "40")) { size ->
                    SizeOption(
                        size = size,
                        isSelected = selectedSize == size,
                        onSelect = { selectedSize = size }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Section
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun SizeOption(
    size: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(48.dp)
            .clip(MaterialTheme.shapes.medium),
        onClick = onSelect,
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = size,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) Color.White else Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun BottomBar(
    product: Product,
    onBuyNowClick: (Long) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp
    ) {
        Button(
            onClick = { onBuyNowClick(product.id) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Buy Now",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $message",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}