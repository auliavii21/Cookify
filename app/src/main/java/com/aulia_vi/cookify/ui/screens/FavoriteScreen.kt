package com.aulia_vi.cookify.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aulia_vi.cookify.ui.viewmodel.FavoriteViewModel
import coil.compose.AsyncImage
import com.aulia_vi.cookify.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController, favoriteViewModel: FavoriteViewModel) {
    val favorites = favoriteViewModel.favorites.collectAsState().value

    Scaffold(topBar = { TopAppBar(title = { Text("Favorites") }) }) { padding ->
        if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(stringResource(R.string.no_favorites))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(favorites) { fav ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            AsyncImage(model = fav.thumb, contentDescription = fav.name, modifier = Modifier.size(92.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(fav.name)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    OutlinedButton(onClick = {
                                        // open detail by id
                                        navController.navigate("detail/${fav.id}")
                                    }) {
                                        Text(stringResource(R.string.view_recipe))
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    OutlinedButton(onClick = { favoriteViewModel.removeFavorite(fav) }) {
                                        Text(stringResource(R.string.remove_favorite))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
