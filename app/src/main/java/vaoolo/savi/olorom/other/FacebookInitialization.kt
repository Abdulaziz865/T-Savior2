package vaoolo.savi.olorom.other

import android.content.Context
import com.facebook.FacebookSdk

class FacebookInitialization(
    private val context: Context
) {

    fun init(fbId : String, fbSecret: String) {
        FacebookSdk.setApplicationId(fbId)
        FacebookSdk.setClientToken(fbSecret)
        FacebookSdk.sdkInitialize(context)
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
    }
}