package com.example.catalogueapp

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object AppNavigation {
    val current: NavHostController
        @Composable
        get() = LocalNavHostController.current

    var navBarVisible by mutableStateOf(false)
}

@Composable
fun ProvideNavHostController(content: @Composable () -> Unit) {
    val navHostController = rememberNavController()
    CompositionLocalProvider(
        LocalNavHostController provides navHostController,
        content = content
    )
}

val LocalNavHostController = staticCompositionLocalOf<NavHostController> {
    error("No NavHostController provided")
}