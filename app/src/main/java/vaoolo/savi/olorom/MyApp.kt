package vaoolo.savi.olorom

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import vaoolo.savi.olorom.other.OneSignalInitialization
import vaoolo.savi.olorom.white.utils.PreferenceHelper
import javax.inject.Inject


@HiltAndroidApp
class MyApp : Application() {

    @Inject
    lateinit var oneSignalInitialization: OneSignalInitialization

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.units(this)
        oneSignalInitialization.init()
    }
}