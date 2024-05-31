package com.sds.someflashlight.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DashboardItemDao {
    @Query("SELECT * FROM dashboard_items")
    suspend fun getAllItems(): List<DashboardItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: DashboardItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<DashboardItem>)
}