package com.aulia_vi.cookify.repository

import com.aulia_vi.cookify.data.api.ApiService
import com.aulia_vi.cookify.data.local.FavoriteDao
import com.aulia_vi.cookify.data.local.FavoriteMeal
import kotlinx.coroutines.flow.Flow

class RecipeRepository(
    private val api: ApiService,
    private val favoriteDao: FavoriteDao
) {
    suspend fun searchMeals(query: String) = api.searchMeals(query)
    suspend fun lookupMeal(id: String) = api.lookupMeal(id)
    suspend fun filterByCategory(cat: String) = api.filterByCategory(cat)
    suspend fun getCategories() = api.getCategories()

    fun favorites(): Flow<List<FavoriteMeal>> = favoriteDao.getAll()

    suspend fun addFavorite(meal: FavoriteMeal) = favoriteDao.insert(meal)
    suspend fun removeFavorite(meal: FavoriteMeal) = favoriteDao.delete(meal)
    suspend fun isFavorite(id: String) = favoriteDao.isFavorite(id)
}
