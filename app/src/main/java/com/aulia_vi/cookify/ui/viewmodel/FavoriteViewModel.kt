package com.aulia_vi.cookify.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aulia_vi.cookify.CookifyApplication
import com.aulia_vi.cookify.data.local.FavoriteMeal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    private val dao = CookifyApplication.database.favoriteDao()

    // full favorites list as StateFlow
    val favorites = dao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // set of favorite ids for quick lookup in UI
    val favoriteIds = favorites.map { list -> list.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun addFavorite(meal: FavoriteMeal) {
        viewModelScope.launch {
            dao.insert(meal)
        }
    }

    fun removeFavorite(meal: FavoriteMeal) {
        viewModelScope.launch {
            dao.delete(meal)
        }
    }

    suspend fun isFavorite(id: String): Boolean {
        return dao.isFavorite(id)
    }
}
