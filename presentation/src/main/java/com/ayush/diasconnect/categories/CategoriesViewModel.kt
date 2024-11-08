package com.ayush.diasconnect.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.Category
import com.ayush.domain.usecases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun onEvent(event: CategoriesEvent) {
        when (event) {
            is CategoriesEvent.LoadCategories -> loadCategories()
            is CategoriesEvent.SearchCategories -> searchCategories(event.query)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getCategoriesUseCase()
                .onSuccess { categories ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        categories = categories
                    ) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = error.message
                    ) }
                }
        }
    }

    private fun searchCategories(query: String) {
        val filteredCategories = _uiState.value.categories.filter {
            it.name.contains(query, ignoreCase = true)
        }
        _uiState.update { it.copy(categories = filteredCategories) }
    }
}

data class CategoriesUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)

sealed class CategoriesEvent {
    object LoadCategories : CategoriesEvent()
    data class SearchCategories(val query: String) : CategoriesEvent()
}

