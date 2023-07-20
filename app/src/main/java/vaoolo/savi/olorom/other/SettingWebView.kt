package vaoolo.savi.olorom.other

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import vaoolo.savi.olorom.Constants
import vaoolo.savi.olorom.Constants.urlCallbackFail

class SettingWebView(private val firebaseInitialization: FirebaseInitialization) {
    private val TAG = "SettingWebView"

    private var listenerWebView: ListenerWebView? = null

    private var mUploadMessage: ValueCallback<Array<Uri>>? = null
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    interface ListenerWebView {
        fun isNotRealUser()
        fun pageLoadFinished()
        fun error()
    }

    fun setupListener(listenerWebView: ListenerWebView) {
        this.listenerWebView = listenerWebView
    }

    private fun setupActivityResult(appCompatActivity: AppCompatActivity) {
        activityResultLauncher = appCompatActivity.activityResultRegistry.register(
            "UploadFile",
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val uri: Uri? = result.data?.data
                    if (uri != null && mUploadMessage != null) {
                        mUploadMessage?.onReceiveValue(arrayOf(uri))
                        mUploadMessage = null
                    }
                }
            }
        }
    }

    fun setupWebView(appCompatActivity: AppCompatActivity, webView: WebView) {
        setupActivityResult(appCompatActivity)

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        Log.d(TAG, "shouldOverrideUrlLoading: 1111")


        webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            allowContentAccess = true
            domStorageEnabled = true
            allowContentAccess = true
            allowFileAccess = true
            userAgentString = webView.settings.userAgentString.replaceAfter(")", "")
            useWideViewPort = true
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (url.contains(Regex(urlCallbackFail))) {
                    Log.d(TAG, "contains: true")
                    listenerWebView?.isNotRealUser()
                }

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                listenerWebView?.pageLoadFinished()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.d(TAG, "onReceivedError: ")
                listenerWebView?.error()
                super.onReceivedError(view, request, error)
            }
        }

        webView.webChromeClient = object : WebChromeClient() {

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d(TAG, "onConsoleMessage: $consoleMessage")
                if (consoleMessage?.message() == "click") {
                    firebaseInitialization.sendEvent()
                    return true
                }
                return false
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                if (mUploadMessage != null) {
                    mUploadMessage?.onReceiveValue(null)
                    mUploadMessage = null
                }

                mUploadMessage = filePathCallback
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                activityResultLauncher?.launch(Intent.createChooser(intent, "File Browser"))

                return true

            }
        }

        webView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK

                && event.action == MotionEvent.ACTION_UP
                && webView.canGoBack()
            ) {
                webView.goBack()
                return@OnKeyListener true
            }
            false
        })

        webView.loadUrl(Constants.url)
    }
}