package vaoolo.savi.olorom.white.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import vaoolo.savi.olorom.R
import vaoolo.savi.olorom.databinding.ActivityWhiteBinding
import vaoolo.savi.olorom.white.utils.PreferenceHelper

@AndroidEntryPoint
class WhiteActivity : AppCompatActivity(R.layout.activity_white) {

    private val binding by viewBinding(ActivityWhiteBinding::bind)
    private lateinit var controllerNav: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        controllerNav = navHostFragment.navController

        when (PreferenceHelper.isStartApp) {
            true -> {
                controllerNav.navigate(R.id.chatFragment)
                controllerNav.popBackStack()
            }
            else -> {
                controllerNav.navigate(R.id.onBoardFragment)
            }
        }
    }
}