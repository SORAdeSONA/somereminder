package com.sds.someflashlight

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.view.WindowMetrics
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sds.someflashlight.adapter.DashboardAdapter
import com.sds.someflashlight.room.AppDatabase
import com.sds.someflashlight.room.DashboardItem
import com.sds.someflashlight.room.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.State


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPrefsManager: SharedPrefsManager

    private lateinit var repository: DashboardRepository

    private var dashboardAdapter: DashboardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        sharedPrefsManager = SharedPrefsManager(this)

        installBottomSheetBehavior()
        setAdapter()

        setDatabase()

        if (sharedPrefsManager.getFirstLoad()) {
            firstLoadDashboardItems()
        }else {
            loadDashboardItems()
        }
    }

    private fun setAdapter() {

        val recyclerView = findViewById<RecyclerView>(R.id.dashboard_rv)

        dashboardAdapter = DashboardAdapter()
        recyclerView.adapter = dashboardAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun setDatabase() {
        val database = AppDatabase.getDatabase(this)
        val dao = database.dashboardItemDao()
        repository = DashboardRepository(dao)
    }

    private fun loadDashboardItems() {

        lifecycleScope.launch {
            dashboardAdapter?.addDashboardItems(repository.getAllItems())
        }
    }

    private fun firstLoadDashboardItems(){

        val newItems = listOf(

            DashboardItem(
                title = "Device info",
                iconResId = R.drawable.img_device_info,
                description = "Explore your device characteristics",
                alerts = 0
            ),

            DashboardItem(
                title = "Calibration of sensors",
                iconResId = R.drawable.img_calibration_of_sensors,
                description = "2 error found",
                alerts = 2
            ),

            DashboardItem(
                title = "App monitoring",
                iconResId = R.drawable.img_app_monitoring,
                description = "No errors found",
                alerts = 0
            ),

            DashboardItem(
                title = "AntiVirus",
                iconResId = R.drawable.img_virus_blue,
                description = "5 errors found",
                alerts = 5
            ),

            DashboardItem(
                title = "Device Memory Information",
                iconResId = R.drawable.device_memory_information_img,
                description = "Clear your phone storage",
                alerts = 0
            ),

            DashboardItem(
                title = "File manager",
                iconResId = R.drawable.img_storage,
                description = "Open file manager",
                alerts = 0
            ),

            DashboardItem(
                title = "Battery info",
                iconResId = R.drawable.img_battery,
                description = "Open battery settings",
                alerts = 0
            )

        )

        lifecycleScope.launch {
            repository.insertItems(newItems)

            sharedPrefsManager.setFirstLoad(false)

            loadDashboardItems()
        }
    }

    private fun installBottomSheetBehavior() {
        val container = findViewById<ConstraintLayout>(R.id.constraintLayout2)
        val bottomSheet = findViewById<CardView>(R.id.bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)


        container.post {
            val location = IntArray(2)
            container.getLocationOnScreen(location)
            val targetViewBottom = location[1] + container.height

            val screenHeight =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    val windowMetrics = windowManager.currentWindowMetrics
                    val insets =
                        windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                    windowMetrics.bounds.height() - insets.top - insets.bottom
                } else {
                    val displayMetrics = DisplayMetrics()
                    windowManager.defaultDisplay.getMetrics(displayMetrics)
                    displayMetrics.heightPixels
                }

            val marginInPixels = resources.getDimensionPixelSize(R.dimen.bottom_sheet_margin)
            val availableHeight = screenHeight - targetViewBottom

            // Ensure the bottom sheet is correctly positioned with a margin of 16dp
            bottomSheetBehavior.peekHeight = availableHeight - marginInPixels

            Log.d("MainActivity", "screenHeight: $screenHeight")
            Log.d("MainActivity", "targetViewBottom: $targetViewBottom")
            Log.d("MainActivity", "peekHeight: ${bottomSheetBehavior.peekHeight}")

            // Set the bottom sheet's top margin
            val layoutParams = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.topMargin = targetViewBottom + marginInPixels
            bottomSheet.layoutParams = layoutParams
        }
        // Set the state change listener to handle expanding/collapsing
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Handle state changes if needed
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Handle slide offset if needed
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        dashboardAdapter = null
    }
}
