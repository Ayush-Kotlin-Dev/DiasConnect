package com.ayush.diasconnect.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.data.repository.ProductRepositoryImpl
import com.ayush.domain.repository.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepositoryImpl: ProductRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val result = productRepositoryImpl.getProducts()
            _uiState.value = when {
                result.isSuccess -> HomeUiState.Success(result.getOrNull() ?: emptyList())
                else -> HomeUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun retryLoading() {
        loadProducts()
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val products: List<Product>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}