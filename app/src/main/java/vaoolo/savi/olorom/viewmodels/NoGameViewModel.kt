package vaoolo.savi.olorom.viewmodels

import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vaoolo.savi.olorom.other.FirebaseInitialization
import vaoolo.savi.olorom.other.OneSignalInitialization
import vaoolo.savi.olorom.other.SettingWebView
import vaoolo.savi.olorom.other.SharedPrefs
import javax.inject.Inject

@HiltViewModel
class NoGameViewModel @Inject constructor(
    private val settingWebView: SettingWebView,
    private val oneSignalInitialization: OneSignalInitialization,
    private val firebaseInitialization: FirebaseInitialization,
    private val sharedPrefs: SharedPrefs
) : ViewModel() {

    fun setupSettingsWebView(
        appCompatActivity: AppCompatActivity,
        webView: WebView,
        listenerWebView: SettingWebView.ListenerWebView
    ) {
        settingWebView.setupListener(listenerWebView)
        settingWebView.setupWebView(appCompatActivity, webView)
    }

    fun isNotRealUser() {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPrefs.isRealUser = false
            oneSignalInitialization.unsubscribePush()
        }
    }

    fun initFirebase() {
        firebaseInitialization.init()
    }
}



