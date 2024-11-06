package com.ayush.diasconnect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.ayush.diasconnect.ui.theme.Blue

class ContainerApp : Screen {
    @Composable
    override fun Content() {
        val showBottomBar = remember { mutableStateOf(true) }
        val initialTab = remember { HomeTab(onNavigator = { showBottomBar.value = it }) }

        TabNavigator(initialTab) { tabNavigator ->
            Scaffold(
                content = { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    AnimatedVisibility(
                        visible = showBottomBar.value,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(durationMillis = 300)
                        ),
                        exit = slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(durationMillis = 300)
                        )
                    ) {
                        NavigationBar(
                            modifier = Modifier.height(73.dp),
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ) {
                            TabNavigationItem(HomeTab(onNavigator = { showBottomBar.value = it }))
                            TabNavigationItem(MoreTab(onNavigator = { showBottomBar.value = it }))
                            TabNavigationItem(CartTab(onNavigator = { showBottomBar.value = it }))
                            TabNavigationItem(ProfileTab(onNavigator = { showBottomBar.value = it }))
                        }
                    }
                }
            )
        }
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
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected) Blue.copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surface
                    ),
                contentAlignment = Alignment.Center
            ) {
                tab.options.icon?.let { painter ->
                    Icon(
                        painter = painter,
                        contentDescription = tab.options.title,
                        tint = if (selected) Blue else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        label = {
            Text(
                text = tab.options.title,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 10.sp,
                color = if (selected) Blue else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Blue,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = Color.Transparent,
        ),
        alwaysShowLabel = false
    )
}