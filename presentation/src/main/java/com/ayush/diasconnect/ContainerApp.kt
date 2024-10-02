package com.ayush.diasconnect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.ayush.diasconnect.tabs.CartTab
import com.ayush.diasconnect.tabs.HomeTab
import com.ayush.diasconnect.tabs.MoreTab
import com.ayush.diasconnect.tabs.ProfileTab

class ContainerApp : Screen {
    @Composable
    override fun Content() {
        TabNavigator(HomeTab) { tabNavigator ->
            val currentTab = tabNavigator.current

            Scaffold(
                content = { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    if (shouldShowBottomBar(currentTab)) {
                        NavigationBar(
                            modifier = Modifier.height(74.dp),
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ) {
                            TabNavigationItem(HomeTab)
                            TabNavigationItem(MoreTab)
                            TabNavigationItem(CartTab)
                            TabNavigationItem(ProfileTab)
                        }
                    }
                }
            )
        }
    }

    private fun shouldShowBottomBar(currentTab: Tab): Boolean {
        return currentTab is HomeTab ||
                currentTab is ProfileTab ||
                currentTab is CartTab ||
                currentTab is MoreTab
    }
}


@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val selected = tabNavigator.current == tab

    NavigationBarItem(
        selected = selected,
        onClick = { tabNavigator.current = tab },
        icon = {
            Box(
                modifier = Modifier
                    .size(48.dp)  // Reduced from 56.dp
                    .clip(CircleShape)
                    .background(if (selected) Color.Red.copy(alpha = 0.1f) else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                tab.options.icon?.let { painter ->
                    Icon(
                        painter = painter,
                        contentDescription = tab.options.title,
                        tint = if (selected) Color.Red else Color.Gray,
                        modifier = Modifier.size(20.dp)  // Reduced from 24.dp
                    )
                }
            }
        },
        label = {
            Text(
                text = tab.options.title,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 10.sp,
                color = if (selected) Color.Red else Color.Gray
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Red,
            unselectedIconColor = Color.Gray,
            indicatorColor = Color.Transparent
        ),
        alwaysShowLabel = false
    )
}