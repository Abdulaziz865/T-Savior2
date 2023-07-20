package vaoolo.savi.olorom.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import vaoolo.savi.olorom.other.SettingWebView
import vaoolo.savi.olorom.other.SharedPrefs
import vaoolo.savi.olorom.viewmodels.NoGameViewModel
import dagger.hilt.android.AndroidEntryPoint
import vaoolo.savi.olorom.R
import vaoolo.savi.olorom.databinding.NoGameBinding
import vaoolo.savi.olorom.white.ui.activity.WhiteActivity
import javax.inject.Inject

@AndroidEntryPoint
class NoGameFragment : Fragment(R.layout.no_game) {

    private val binding by viewBinding(NoGameBinding::bind)
    private val viewModel: NoGameViewModel by viewModels()

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkUser()
    }

    private fun checkUser() {
        if (sharedPrefs.isFirstOpen) {
            viewModel.initFirebase()
        }

        if (sharedPrefs.isRealUser) {
            initializationUI()
            setupSettingsWebView()
        } else
            logicUserFail()
    }

    private fun initializationUI() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun checkInternet() {
        try {
            val connectionManager =
                activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkInfo = connectionManager.activeNetworkInfo
            if (!(networkInfo != null && networkInfo.isConnected)) {
                logicUserFail()
            }

        } catch (e: Exception) {
            logicUserFail()
        }
    }

    private fun logicUserFail() {
        requireActivity().startActivity(Intent(requireContext(), WhiteActivity::class.java))
        requireActivity().finish()
    }

    private fun setupSettingsWebView() {
        val listener = object : SettingWebView.ListenerWebView {
            override fun isNotRealUser() {
                viewModel.isNotRealUser()
                logicUserFail()
            }

            override fun pageLoadFinished() {
                if (sharedPrefs.isRealUser) {
                    binding.constraintProgress.isVisible = false
                    binding.webMain.isVisible = true
                }
            }

            override fun error() {
                checkInternet()
            }
        }

        viewModel.setupSettingsWebView(
            requireActivity() as AppCompatActivity,
            binding.webMain,
            listener
        )
    }
}