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
import com.ayush.diasconnect.cart.CartScreen
import com.ayush.domain.model.Product

data class ProductDetailScreen(val productId: Long) : Screen {
    @Composable
    override fun Content() {
        val viewModel: ProductDetailViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(productId) {
            viewModel.loadProduct(productId)
        }

        ProductDetailContent(uiState, viewModel::addItemToCart)
    }
}

@Composable
private fun ProductDetailContent(
    uiState: ProductDetailUiState,
    onAddToCart: (Long, Double, Int) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        bottomBar = {
            when (uiState) {
                is ProductDetailUiState.Success -> {
                    BottomBar(uiState.product, uiState.isAddingToCart, onAddToCart)
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
                    ProductDetails(uiState.product, navigator::pop)

                    if (uiState.addToCartSuccess) {
                        LaunchedEffect(Unit) {
                            // Show a success message or navigate to cart
                            // For example:
                        navigator.push(CartScreen())
                        }
                    }

                    uiState.error?.let { error ->
                        ErrorSnackbar(error)
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
        ProductImage( isFavorite , product.images.firstOrNull(), product.name, onBackClick , ) {
            isFavorite = !isFavorite
        }

        ProductInfo(product, isFavorite)

        SizeSelector(
            sizes = listOf("8", "10", "38", "40"),
            selectedSize = selectedSize,
            onSizeSelected = { selectedSize = it }
        )

        DescriptionSection(product.description)
    }
}

@Composable
private fun ProductImage(
    isFavorite: Boolean,
    imageUrl: String?,
    productName: String,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = productName,
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
                onClick = onFavoriteClick,
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
}

@Composable
private fun ProductInfo(product: Product, isFavorite: Boolean) {
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
                text = "₹${product.price}",
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
    }
}

@Composable
private fun SizeSelector(
    sizes: List<String>,
    selectedSize: String?,
    onSizeSelected: (String) -> Unit
) {
    Column {
        Text(
            text = "Size",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(sizes) { size ->
                SizeOption(
                    size = size,
                    isSelected = selectedSize == size,
                    onSelect = { onSizeSelected(size) }
                )
            }
        }
    }
}

@Composable
private fun DescriptionSection(description: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
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
    isAddingToCart: Boolean,
    onAddToCart: (Long, Double, Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp
    ) {
        Button(
            onClick = { onAddToCart(product.id, product.price, 1) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = MaterialTheme.shapes.medium,
            enabled = !isAddingToCart
        ) {
            if (isAddingToCart) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Buy Now",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
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

@Composable
private fun ErrorSnackbar(message: String) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        snackbarHostState.showSnackbar(message)
    }

    SnackbarHost(hostState = snackbarHostState)
}