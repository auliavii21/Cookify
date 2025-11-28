package com.aulia_vi.cookify.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aulia_vi.cookify.R
import com.aulia_vi.cookify.ui.components.RecipeCard
import com.aulia_vi.cookify.ui.viewmodel.FavoriteViewModel
import com.aulia_vi.cookify.ui.viewmodel.MealViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MealViewModel,
    favoriteViewModel: FavoriteViewModel
) {

    val meals by viewModel.meals.collectAsState()
    val favIds by favoriteViewModel.favoriteIds.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var searchVisible by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Beef") }

    val config = LocalConfiguration.current
    val isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE


    if (isLandscape) {
        var showCategoryMenu by remember { mutableStateOf(false) }

        Row(modifier = Modifier.fillMaxSize()) {

            NavigationRail(
                header = {},
                modifier = Modifier.padding(4.dp)
            ) {

                // SEARCH
                NavigationRailItem(
                    selected = false,
                    onClick = { searchVisible = !searchVisible },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                )

                // FAVORITE
                NavigationRailItem(
                    selected = false,
                    onClick = { navController.navigate("favorites") },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") }
                )

                // CATEGORY DROPDOWN
                Box {
                    NavigationRailItem(
                        selected = false,
                        onClick = { showCategoryMenu = true },
                        icon = { Icon(Icons.Default.Menu, contentDescription = "Category") }
                    )

                    DropdownMenu(
                        expanded = showCategoryMenu,
                        onDismissRequest = { showCategoryMenu = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    selectedCategory = cat
                                    showCategoryMenu = false
                                    query = ""
                                    viewModel.filterByCategory(cat)
                                }
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {

                // SEARCH BAR (slide down)
                AnimatedVisibility(visible = searchVisible) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            viewModel.loadMeals(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        label = { Text(stringResource(R.string.search_hint)) },
                        singleLine = true
                    )
                }

                // GRID = 3 columns in Landscape
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(meals, key = { it.idMeal }) { meal ->
                        RecipeCard(
                            meal = meal,
                            isFavorited = favIds.contains(meal.idMeal),
                            onClick = { navController.navigate("detail/${meal.idMeal}") },
                            onToggleFavorite = { fav ->
                                if (favIds.contains(fav.id)) {
                                    favoriteViewModel.removeFavorite(fav)
                                } else {
                                    favoriteViewModel.addFavorite(fav)
                                }
                            }
                        )
                    }
                }
            }
        }

        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cookify 🍳") },
                actions = {
                    IconButton(onClick = { searchVisible = !searchVisible }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { navController.navigate("favorites") }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // SEARCH (portrait)
            AnimatedVisibility(visible = searchVisible) {
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        viewModel.loadMeals(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text(stringResource(R.string.search_hint)) },
                    singleLine = true
                )
            }

            // CATEGORY SECTION (portrait only)
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(categories.size) { index ->
                    val cat = categories[index]
                    AssistChip(
                        onClick = {
                            selectedCategory = cat
                            query = ""
                            viewModel.filterByCategory(cat)
                        },
                        label = { Text(cat) },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp)
                    )
                }
            }

            // GRID 2 columns portrait
            AnimatedVisibility(visible = meals.isNotEmpty(), enter = fadeIn()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(meals, key = { it.idMeal }) { meal ->
                        RecipeCard(
                            meal = meal,
                            isFavorited = favIds.contains(meal.idMeal),
                            onClick = { navController.navigate("detail/${meal.idMeal}") },
                            onToggleFavorite = { fav ->
                                if (favIds.contains(fav.id)) {
                                    favoriteViewModel.removeFavorite(fav)
                                } else {
                                    favoriteViewModel.addFavorite(fav)
                                }
                            }
                        )
                    }
                }
            }

            if (meals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.no_results))
                }
            }
        }
    }
}
