package vaoolo.savi.olorom.other

import android.app.Application
import android.os.Bundle
import android.util.Log
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import vaoolo.savi.olorom.Constants

class FirebaseInitialization(
    private val application: Application,
    private val facebookInitialization: FacebookInitialization
) {

    private val TAG = "FirebaseInitialization"
    fun init() {

        FirebaseApp.initializeApp(application)

        val remoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val fbId = remoteConfig.getString(Constants.firebaseFbIdKey)
                    val fbSecret = remoteConfig.getString(Constants.firebaseFbSecretKey)
                    Log.d(TAG, "init fbId: $fbId")
                    Log.d(TAG, "init fbSecret: $fbSecret ")

                    if (fbId.isNotEmpty() && fbSecret.isNotEmpty())
                        facebookInitialization.init(fbId, fbSecret)

                }
            }

    }

    fun sendEvent() {
        AppEventsLogger.activateApp(application)
        val logger = AppEventsLogger.newLogger(application.applicationContext)
        val params = Bundle()
        params.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, "Email")
        logger.logEvent("EVENT_NAME_COMPLETED_REGISTRATION", params)
        val firebaseAnalytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.METHOD, "email")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)
    }
}