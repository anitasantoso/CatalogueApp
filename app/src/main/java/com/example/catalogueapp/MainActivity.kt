package com.example.catalogueapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.catalogueapp.ui.screen.NavGraph
import com.example.catalogueapp.ui.screen.TopLevelScreens
import com.example.catalogueapp.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                ProvideNavHostController {
                    AppScaffold()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    Scaffold(

        // TODO make dynamic topbar
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        "",
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis,
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                })
//        },
        bottomBar = { BottomAppBar() }, content = {
            NavGraph(it)
        })
}

@Composable
fun BottomAppBar(navController: NavController = AppNavigation.current) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar() {
        TopLevelScreens.forEach() {
            NavigationBarItem(
                selected = currentRoute == it.route,
                icon = {
                    Icon(
                        requireNotNull(it.icon),
                        it.route,
                        Modifier.size(ButtonDefaults.IconSize)
                    )
                },
                label = { Text(it.route) },
                onClick = {
                    navController.navigate(it.route) {
                        launchSingleTop = true
                    }
                })
        }
    }
}