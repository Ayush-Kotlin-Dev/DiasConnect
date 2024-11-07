package com.ayush.diasconnect.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.ayush.diasconnect.home.ProductScreen
import com.ayush.diasconnect.profile.ProfileScreen

class ProfileTab(private val onNavigator: (Boolean) -> Unit): Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Profile"
            val icon = rememberVectorPainter(Icons.Default.Person)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }


    @Composable
    override fun Content() {
        Navigator(screen = ProfileScreen()){navigator ->
            LaunchedEffect(navigator.lastItem){
                onNavigator(navigator.lastItem is ProfileScreen)
            }
            SlideTransition(navigator = navigator)
        }
    }
}