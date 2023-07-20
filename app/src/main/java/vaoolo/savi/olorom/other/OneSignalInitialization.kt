package vaoolo.savi.olorom.other

import android.content.Context
import android.content.Intent
import com.onesignal.OneSignal
import vaoolo.savi.olorom.Constants
import vaoolo.savi.olorom.MainActivity

class OneSignalInitialization(
    private val context: Context,
    private val sharedPrefs: SharedPrefs
) {

    fun unsubscribePush() {
        OneSignal.disablePush(true)
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
    }

    fun init() {
        if (sharedPrefs.isRealUser){
            initOneSignal()
        }

        if (sharedPrefs.isFirstInitOnesignal){
            setupOneSignal()
            sharedPrefs.isFirstInitOnesignal = false
        }

    }

    private fun initOneSignal() {
        OneSignal.initWithContext(context)
        OneSignal.promptForPushNotifications()

        OneSignal.setNotificationOpenedHandler {
            context.startActivity(
                Intent(
                    context,
                    MainActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    private fun setupOneSignal(){
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.setAppId(Constants.onesignalId)

    }


}