package com.ayush.diasconnect.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.Product
import com.ayush.domain.model.Result
import com.ayush.domain.usecases.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onSearchQueryChange(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(searchQuery = query, isLoading = true) }
            performSearch(query)
        }
    }

    private suspend fun performSearch(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), isLoading = false) }
            return
        }

        when (val result = searchProductsUseCase(query)) {
            is Result.Success -> {
                _uiState.update { it.copy(searchResults = result.data, isLoading = false, error = null) }
            }
            is Result.Error -> {
                _uiState.update { it.copy(searchResults = emptyList(), isLoading = false, error = result.exception.message) }
            }

            Result.Loading -> TODO()
        }
    }
}

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)