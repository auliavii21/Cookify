package com.aulia_vi.cookify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aulia_vi.cookify.ui.screens.DetailScreen
import com.aulia_vi.cookify.ui.screens.FavoritesScreen
import com.aulia_vi.cookify.ui.screens.HomeScreen
import com.aulia_vi.cookify.ui.theme.CookifyTheme
import com.aulia_vi.cookify.ui.viewmodel.FavoriteViewModel
import com.aulia_vi.cookify.ui.viewmodel.MealViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CookifyTheme {
                val navController = rememberNavController()

                // ViewModels
                val mealViewModel: MealViewModel = viewModel()
                val favoriteViewModel: FavoriteViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            navController = navController,
                            viewModel = mealViewModel,
                            favoriteViewModel = favoriteViewModel
                        )
                    }

                    composable(
                        route = "detail/{mealId}",
                        arguments = listOf(navArgument("mealId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
                        DetailScreen(
                            mealId = mealId,
                            mealViewModel = mealViewModel,
                            favoriteViewModel = favoriteViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("favorites") {
                        FavoritesScreen(
                            navController = navController,
                            favoriteViewModel = favoriteViewModel
                        )
                    }
                }
            }
        }
    }
}
