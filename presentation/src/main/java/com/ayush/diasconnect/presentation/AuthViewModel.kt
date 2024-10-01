package com.ayush.diasconnect.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.data.model.AuthResponse
import com.ayush.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signUp(name, email, password)
            handleAuthResult(result)
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signIn(email, password)
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: Result<AuthResponse>) {
        _authState.value = when {
            result.isSuccess -> {
                val response = result.getOrNull()
                if (response?.data != null) {
                    AuthState.Success(response)
                } else {
                    AuthState.Error(response?.errorMessage ?: "Unknown error occurred isSuccess block")
                }
            }
            result.isFailure -> {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown error occurred isFailure block")
            }
            else -> AuthState.Error("Unknown error occurred else block")
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val response: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}