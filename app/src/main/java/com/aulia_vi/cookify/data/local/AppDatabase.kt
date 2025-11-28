package com.aulia_vi.cookify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMeal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
}
