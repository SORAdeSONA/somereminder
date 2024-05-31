package com.sds.someflashlight.room

class DashboardRepository(private val dao: DashboardItemDao) {

    suspend fun getAllItems(): List<DashboardItem> {
        return dao.getAllItems()
    }

    suspend fun insertItem(item: DashboardItem) {
        dao.insertItem(item)
    }

    suspend fun insertItems(items: List<DashboardItem>) {
        dao.insertItems(items)
    }

}