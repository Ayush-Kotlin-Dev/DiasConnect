package com.ayush.diasconnect.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.ayush.diasconnect.detail_screen.ProductDetailScreen
import com.ayush.diasconnect.home.PopularProductCard
import com.ayush.domain.model.Product
import kotlinx.coroutines.delay

class SearchScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: SearchViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        SearchScreenContent(
            uiState = uiState,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onBackClick = { navigator.pop() },
            onProductClick = { navigator.push(ProductDetailScreen(it.id)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    uiState: SearchUiState,
    onSearchQueryChange: (String) -> Unit,
    onBackClick: () -> Unit, 
    onProductClick: (Product) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Search") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = onSearchQueryChange,
            focusRequester = focusRequester
        )
        SearchResults(
            uiState = uiState,
            onProductClick = onProductClick
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .padding(16.dp),
        placeholder = { Text("Search products") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        singleLine = true
    )
}

@Composable
fun SearchResults(
    uiState: SearchUiState,
    onProductClick: (Product) -> Unit
) {
    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            Text(
                text = "Error: ${uiState.error}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
        uiState.searchResults.isEmpty() && uiState.searchQuery.isNotBlank() -> {
            Text(
                text = "No results found",
                modifier = Modifier.padding(16.dp)
            )
        }
        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.searchResults) { product ->
                    PopularProductCard(
                        product = product,
                        onClick = { onProductClick(product) }
                    )
                }
            }
        }
    }
}