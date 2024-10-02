package com.ayush.diasconnect.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.ayush.diasconnect.home.ProductScreen


class  AuthScreen() : Screen{
    @Composable
    override fun Content() {
        val viewModel: AuthViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()
        val focusManager = LocalFocusManager.current
        val naviagtor = LocalNavigator.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { naviagtor?.push(ProductScreen()) }) {
                Text("Go to Product Screen")
            }
            if (uiState.isSignUp) {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.onEvent(AuthEvent.UpdateName(it)) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEvent(AuthEvent.UpdateEmail(it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onEvent(AuthEvent.UpdatePassword(it)) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (uiState.isSignUp) {
                        viewModel.onEvent(AuthEvent.SignUp(uiState.name, uiState.email, uiState.password))
                    } else {
                        viewModel.onEvent(AuthEvent.SignIn(uiState.email, uiState.password))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isSignUp) "Sign Up" else "Sign In")
            }

            TextButton(
                onClick = { viewModel.onEvent(AuthEvent.ToggleAuthMode) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isSignUp) "Already have an account? Sign In" else "Don't have an account? Sign Up")
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            uiState.error?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            uiState.user?.let { user ->
                Text("Welcome, ${user.name}!")
            }
        }
    }
}