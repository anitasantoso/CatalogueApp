package com.example.catalogueapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object AppNavigation {
    val current: NavHostController
        @Composable
        get() = LocalNavHostController.current
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