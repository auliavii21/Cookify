package com.aulia_vi.cookify.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aulia_vi.cookify.CookifyApplication
import com.aulia_vi.cookify.data.api.Meal
import com.aulia_vi.cookify.data.api.RetrofitClient
import com.aulia_vi.cookify.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealViewModel : ViewModel() {

    private val repo = RecipeRepository(
        RetrofitClient.api,
        CookifyApplication.database.favoriteDao()
    )

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    init {
        loadMeals("")
        loadCategories()
    }

    fun loadMeals(query: String = "") {
        viewModelScope.launch {
            try {
                val resp = repo.searchMeals(query)
                _meals.value = resp.meals ?: emptyList()
            } catch (e: Exception) {
                _meals.value = emptyList()
            }
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            try {
                val resp = repo.filterByCategory(category)
                _meals.value = resp.meals ?: emptyList()
            } catch (e: Exception) {
                _meals.value = emptyList()
            }
        }
    }

    // ⭐ NEW FUNCTION (agar cocok dengan HomeScreen)
    fun loadMealsByCategory(category: String) {
        filterByCategory(category)
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val resp = repo.getCategories()
                val cats = resp.categories?.mapNotNull { it.strCategory } ?: emptyList()
                _categories.value = cats
            } catch (e: Exception) {
                _categories.value = emptyList()
            }
        }
    }

    suspend fun fetchMealById(id: String): Meal? {
        return try {
            repo.lookupMeal(id).meals?.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
}
