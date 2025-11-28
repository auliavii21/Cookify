@file:OptIn(ExperimentalMaterial3Api::class)

package com.aulia_vi.cookify.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aulia_vi.cookify.R
import com.aulia_vi.cookify.data.api.Meal
import com.aulia_vi.cookify.data.local.FavoriteMeal
import com.aulia_vi.cookify.ui.viewmodel.FavoriteViewModel
import com.aulia_vi.cookify.ui.viewmodel.MealViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    mealId: String,
    mealViewModel: MealViewModel,
    favoriteViewModel: FavoriteViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var meal by remember { mutableStateOf<Meal?>(null) }
    var isFavorited by remember { mutableStateOf(false) }

    LaunchedEffect(mealId) {
        meal = mealViewModel.fetchMealById(mealId)
        meal?.let {
            isFavorited = favoriteViewModel.isFavorite(it.idMeal)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)) {

            TopAppBar(
                title = { Text(meal?.strMeal ?: "Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )

            meal?.let { m ->
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(model = m.strMealThumb, contentDescription = m.strMeal, modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(m.strMeal ?: "", style = MaterialTheme.typography.titleLarge)
                Text(listOfNotNull(m.strArea, m.strCategory).joinToString(" • "), style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(12.dp))
                Text(stringResource(R.string.ingredients), style = MaterialTheme.typography.titleMedium)
                getIngredientsList(m).forEach { (ing, measure) ->
                    Text("• $ing — ${measure ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(stringResource(R.string.instructions), style = MaterialTheme.typography.titleMedium)
                Text(m.strInstructions ?: "-", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = {
                        m.strYoutube?.let { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.view_youtube))
                    }

                    OutlinedButton(onClick = {
                        scope.launch {
                            val fav = FavoriteMeal(id = m.idMeal, name = m.strMeal ?: "", thumb = m.strMealThumb ?: "")
                            if (isFavorited) favoriteViewModel.removeFavorite(fav) else favoriteViewModel.addFavorite(fav)
                            isFavorited = !isFavorited
                        }
                    }) {
                        Text(if (isFavorited) "Favorited" else "add favorite")
                    }
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Loading...")
                }
            }

        }
    }
}

fun getIngredientsList(meal: Meal): List<Pair<String, String?>> {
    val ingredients = mutableListOf<Pair<String, String?>>()
    val ingVals = listOf(
        meal.strIngredient1, meal.strIngredient2, meal.strIngredient3, meal.strIngredient4, meal.strIngredient5,
        meal.strIngredient6, meal.strIngredient7, meal.strIngredient8, meal.strIngredient9, meal.strIngredient10,
        meal.strIngredient11, meal.strIngredient12, meal.strIngredient13, meal.strIngredient14, meal.strIngredient15,
        meal.strIngredient16, meal.strIngredient17, meal.strIngredient18, meal.strIngredient19, meal.strIngredient20
    )
    val measureVals = listOf(
        meal.strMeasure1, meal.strMeasure2, meal.strMeasure3, meal.strMeasure4, meal.strMeasure5,
        meal.strMeasure6, meal.strMeasure7, meal.strMeasure8, meal.strMeasure9, meal.strMeasure10,
        meal.strMeasure11, meal.strMeasure12, meal.strMeasure13, meal.strMeasure14, meal.strMeasure15,
        meal.strMeasure16, meal.strMeasure17, meal.strMeasure18, meal.strMeasure19, meal.strMeasure20
    )

    for (i in ingVals.indices) {
        val ing = ingVals[i]?.trim()
        val measure = measureVals.getOrNull(i)?.trim()
        if (!ing.isNullOrBlank()) ingredients.add(Pair(ing, measure))
    }
    return ingredients
}
