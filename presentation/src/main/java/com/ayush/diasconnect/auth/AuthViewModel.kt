package com.ayush.diasconnect.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.AuthResponse
import com.ayush.domain.model.AuthResponseData
import com.ayush.domain.model.Product
import com.ayush.domain.repository.AuthRepository
import com.ayush.domain.usecases.SignInUseCase
import com.ayush.domain.usecases.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// AuthViewModel.kt
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private fun updateState(update: (AuthUiState) -> AuthUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.SignIn -> signIn(event.email, event.password)
            is AuthEvent.SignUp -> signUp(event.name, event.email, event.password)
            is AuthEvent.ClearError -> updateState { it.copy(error = null) }
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, error = null) }

            signUpUseCase(name, email, password)
                .onSuccess { response ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            user = response.data,
                            isAuthenticated = response.data != null
                        )
                    }
                }
                .onError { error ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, error = null) }

            signInUseCase(email, password)
                .onSuccess { response ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            user = response.data,
                            isAuthenticated = response.data != null
                        )
                    }
                }
                .onError { error ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: AuthResponseData? = null,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)


sealed class AuthEvent {
    data class SignIn(val email: String, val password: String) : AuthEvent()
    data class SignUp(
        val name: String,
        val email: String,
        val password: String
    ) : AuthEvent()
    object ClearError : AuthEvent()
}