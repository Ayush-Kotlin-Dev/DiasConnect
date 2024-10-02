package com.ayush.diasconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.SlideTransition
import com.ayush.diasconnect.auth.AuthScreen
import com.ayush.diasconnect.home.ProductScreen
import com.ayush.diasconnect.ui.theme.DiasConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            DiasConnectTheme {
                Navigator(screen = AuthScreen()) { navigator ->
                    FadeTransition(navigator = navigator)
                }
            }
        }
    }
}

