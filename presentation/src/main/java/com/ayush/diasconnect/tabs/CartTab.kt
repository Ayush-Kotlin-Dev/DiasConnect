package com.ayush.diasconnect.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.ayush.diasconnect.cart.CartScreen

class CartTab(
    private val onNavigator: (Boolean) -> Unit
) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Cart"
            val icon = rememberVectorPainter(Icons.Default.ShoppingCart)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
         Navigator(screen = CartScreen()){Navigator ->
             SlideTransition(navigator = Navigator)
         }
    }
}