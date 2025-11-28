package com.aulia_vi.cookify.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aulia_vi.cookify.data.api.Meal
import com.aulia_vi.cookify.data.local.FavoriteMeal

@Composable
fun RecipeCard(
    meal: Meal,
    isFavorited: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: (FavoriteMeal) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column {

            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(meal.strMealThumb)
                        .crossfade(true)
                        .build(),
                    contentDescription = meal.strMeal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = {
                        onToggleFavorite(
                            FavoriteMeal(
                                id = meal.idMeal,
                                name = meal.strMeal ?: "",
                                thumb = meal.strMealThumb ?: ""
                            )
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    if (isFavorited) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favorited", tint = Color.Red)
                    } else {
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorite", tint = Color.White)
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {

                Text(
                    text = meal.strMeal ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onClick() }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "View Recipe",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
