package com.example.catalogueapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.catalogueapp.ui.screen.Screen

object AppNavigation {
    val navController: NavHostController
        @Composable
        get() = LocalNavHostController.current

    private val bottomBarRoutes = listOf(
        Screen.Products, Screen.Favourites, Screen.Settings,
        Screen.Info
    ).map { it.route }

    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes
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