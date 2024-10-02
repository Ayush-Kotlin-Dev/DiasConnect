package com.ayush.diasconnect.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.domain.model.AuthResponseData
import com.ayush.domain.usecases.SignInUseCase
import com.ayush.domain.usecases.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.SignIn -> signIn(event.email, event.password)
            is AuthEvent.SignUp -> signUp(event.name, event.email, event.password)
            is AuthEvent.UpdateName -> updateName(event.name)
            is AuthEvent.UpdateEmail -> updateEmail(event.email)
            is AuthEvent.UpdatePassword -> updatePassword(event.password)
            AuthEvent.ClearError -> clearError()
            AuthEvent.ToggleAuthMode -> toggleAuthMode()
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            signUpUseCase(name, email, password)
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = response.data,
                            isAuthenticated = response.data != null
                        )
                    }
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            signInUseCase(email, password)
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = response.data,
                            isAuthenticated = response.data != null
                        )
                    }
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }

    private fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    private fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun toggleAuthMode() {
        _uiState.update { it.copy(isSignUp = !it.isSignUp) }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: AuthResponseData? = null,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val isSignUp: Boolean = false,
    val name: String = "",
    val email: String = "",
    val password: String = ""
)

sealed class AuthEvent {
    data class SignIn(val email: String, val password: String) : AuthEvent()
    data class SignUp(val name: String, val email: String, val password: String) : AuthEvent()
    data class UpdateName(val name: String) : AuthEvent()
    data class UpdateEmail(val email: String) : AuthEvent()
    data class UpdatePassword(val password: String) : AuthEvent()
    object ClearError : AuthEvent()
    object ToggleAuthMode : AuthEvent()
}