package vaoolo.savi.olorom.white.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

object PreferenceHelper {

    private lateinit var preferences: SharedPreferences
    private lateinit var preferencesColor: SharedPreferences

    fun units(context: Context) {
        preferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
        preferencesColor = context.getSharedPreferences("sharedColor", Context.MODE_PRIVATE)
    }

    var isStartApp: Boolean
        get() = preferences.getBoolean("start", false)
        set(value) = preferences.edit().putBoolean("start", value).apply()

    var isColorPicker: Int
        get() = preferencesColor.getInt("color", Color.WHITE)
        set(value) = preferencesColor.edit().putInt("color", value).apply()
}