package com.ayush.diasconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.ayush.diasconnect.presentation.AuthScreen
import com.ayush.diasconnect.ui.theme.DiasConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            DiasConnectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthScreen()
                }
            }
        }
    }
}

