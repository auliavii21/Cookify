package com.aulia_vi.cookify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteMeal(
    @PrimaryKey val id: String,
    val name: String,
    val thumb: String
)
