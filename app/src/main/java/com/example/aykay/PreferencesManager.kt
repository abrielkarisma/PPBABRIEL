package com.example.aykay

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharePreferences: SharedPreferences =
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharePreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String): String {
        return sharePreferences.getString(key, "") ?: ""
    }
}