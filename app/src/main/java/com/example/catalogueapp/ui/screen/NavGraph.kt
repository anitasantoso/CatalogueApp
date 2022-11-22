package com.example.catalogueapp.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.catalogueapp.AppNavigation
import com.example.catalogueapp.model.Product
import com.google.accompanist.pager.ExperimentalPagerApi

sealed class Screen(val route: String, val icon: ImageVector?) {
    object Products : Screen("products", Icons.Outlined.Home)
    object Favourites : Screen("favourites", Icons.Outlined.Favorite)
    object Settings : Screen("settings", Icons.Outlined.Settings)
    object Info : Screen("info", Icons.Outlined.Info)
    object Details : Screen("details", null)
    object Welcome : Screen("welcome", null)
}

object ScreenArg {
    const val Category = "category"
    const val Product = "product"
}

val TopLevelScreens = listOf(Screen.Products, Screen.Favourites, Screen.Settings, Screen.Info)

fun NavController.navigate(screen: Screen, arg: String? = null) {
    var route = screen.route
    arg?.let {
        route += "/${arg}"
    }
    navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavGraph(padding: PaddingValues, navController: NavHostController = AppNavigation.navController) {
    NavHost(navController, Screen.Welcome.route) {

        // show all products
        composable(Screen.Products.route) {
            ProductsScreen(padding,null, { product ->

                // on product selected
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    ScreenArg.Product,
                    product
                )
                navController.navigate(Screen.Details)
            })
        }

        // show products for a given category
        composable(
            "${Screen.Products.route}/{${ScreenArg.Category}}",
            listOf(navArgument(ScreenArg.Category) { type = NavType.StringType })
        ) {
            val args = it.arguments
            val category = args?.getString(ScreenArg.Category)

            // show all products or for a given category if not null
            ProductsScreen(padding, category, { product ->

                // on product selected
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    ScreenArg.Product,
                    product
                )
                navController.navigate(Screen.Details)
            })
        }

        composable(Screen.Favourites.route) {
            FavouritesScreen()
        }

        composable(Screen.Settings.route) {
            InfoScreen()
        }

        composable(Screen.Info.route) {
            InfoScreen()
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen() {
                navController.navigate(Screen.Products)
            }
        }

        composable(Screen.Details.route) {
            val product =
                navController.previousBackStackEntry?.savedStateHandle?.get<Product>(ScreenArg.Product)

            // if back is pressed, product is null - handle null product in detail screen
            DetailScreen(padding, product)
        }
    }
}
