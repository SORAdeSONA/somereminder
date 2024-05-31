package com.sds.someflashlight

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SharedPrefsManager(context: Context) {

    var sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getFirstLoad() : Boolean = sharedPrefs.getBoolean(FIRST_LOAD_KEY, true)

    fun setFirstLoad(boolean: Boolean) {
        val editor = sharedPrefs.edit()
        editor.putBoolean(FIRST_LOAD_KEY, boolean)
        editor.apply()
    }

    companion object {
        private const val FIRST_LOAD_KEY = "isFirstLoad"
    }
}