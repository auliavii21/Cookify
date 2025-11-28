package com.aulia_vi.cookify.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aulia_vi.cookify.R

// Simple local category
data class Category(val name: String, val icon: Int)

@Composable
fun CategorySection(
    selected: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf(
        Category("Beef", R.drawable.cat_beef),
        Category("Chicken", R.drawable.cat_chicken),
        Category("Seafood", R.drawable.cat_seafood),
        Category("Dessert", R.drawable.cat_dessert),
        Category("Vegetarian", R.drawable.cat_vegetarian),
        Category("Pasta", R.drawable.cat_pasta),
    )

    LazyRow(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { cat ->
            CategoryItem(
                name = cat.name,
                imageRes = cat.icon,
                selected = selected == cat.name,
                onClick = { onCategorySelected(cat.name) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    name: String,
    imageRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = ""
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = name,
            modifier = Modifier
                .size(65.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
