package com.aulia_vi.cookify

import android.app.Application
import androidx.room.Room
import com.aulia_vi.cookify.data.local.AppDatabase

class CookifyApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "cookify.db"
        ).build()
    }
}