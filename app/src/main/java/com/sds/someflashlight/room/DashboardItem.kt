package com.sds.someflashlight.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dashboard_items")
data class DashboardItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val iconResId: Int,
    val description: String,
    val alerts: Int
)