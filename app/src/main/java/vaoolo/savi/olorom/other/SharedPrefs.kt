package vaoolo.savi.olorom.other

import android.content.Context

class SharedPrefs(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var isRealUser
        get() = sharedPreferences.getBoolean("isRealUser", true)
        set(value) = sharedPreferences.edit().putBoolean("isRealUser", value).apply()

    var isFirstOpen
        get() = sharedPreferences.getBoolean("isFirstOpen", true)
        set(value) = sharedPreferences.edit().putBoolean("isFirstOpen", value).apply()

    var isFirstInitOnesignal
        get() = sharedPreferences.getBoolean("isFirstInitOnesignal", true)
        set(value) = sharedPreferences.edit().putBoolean("isFirstInitOnesignal", value).apply()
}