package com.sds.someflashlight.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DashboardItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dashboardItemDao(): DashboardItemDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                .fallbackToDestructiveMigration()
                .build()
    }
}