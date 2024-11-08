package com.ayush.diasconnect.categories


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.ayush.diasconnect.components.LoadingIndicator
import com.ayush.diasconnect.components.ErrorView
import com.ayush.domain.model.Category
import com.ayush.domain.model.SubCategory

class CategoriesScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: CategoriesViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        CategoriesContent(
            uiState = uiState,
            onCategoryClick = { /* Handle category click */ },
            onSubCategoryClick = { /* Handle subcategory click */ },
            onRefresh = { viewModel.onEvent(CategoriesEvent.LoadCategories) },
            onSearchClick = { /* Handle search click */ }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesContent(
    uiState: CategoriesUiState,
    onCategoryClick: (Long) -> Unit,
    onSubCategoryClick: (Long) -> Unit,
    onRefresh: () -> Unit,
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("All Categories", style = MaterialTheme.typography.titleLarge) },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.error != null -> ErrorView(error = uiState.error, onRetry = onRefresh)
            uiState.categories.isEmpty() -> EmptyState()
            else -> CategoryList(
                categories = uiState.categories,
                onCategoryClick = onCategoryClick,
                onSubCategoryClick = onSubCategoryClick
            )
        }
    }
}

@Composable
fun CategoryList(
    categories: List<Category>,
    onCategoryClick: (Long) -> Unit,
    onSubCategoryClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category = category, onClick = { onCategoryClick(category.id) })
            if (category.subCategories.isNotEmpty()) {
                SubCategoryGrid(subCategories = category.subCategories, onSubCategoryClick = onSubCategoryClick)
            }
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
        }
        item {
            SpotlightSection()
        }
    }
}

@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = category.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        if (category.subCategories.isNotEmpty()) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SubCategoryGrid(subCategories: List<SubCategory>, onSubCategoryClick: (Long) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(subCategories) { subCategory ->
            SubCategoryItem(subCategory = subCategory, onClick = { onSubCategoryClick(subCategory.id) })
        }
    }
}

@Composable
fun SubCategoryItem(subCategory: SubCategory, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                model = subCategory.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = subCategory.name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SpotlightSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "In the Spotlight",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SpotlightItem("Diwali Specials", "https://example.com/diwali.jpg")
            SpotlightItem("New Launches", "https://example.com/new_launches.jpg")
            SpotlightItem("Premium Store", "https://example.com/premium.jpg")
        }
    }
}

@Composable
fun SpotlightItem(title: String, imageUrl: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Card(
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No categories found",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}