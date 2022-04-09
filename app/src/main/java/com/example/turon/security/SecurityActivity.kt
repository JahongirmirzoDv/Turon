package com.example.turon.security

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.turon.R
import com.example.turon.data.api2.ApiClient2
import com.example.turon.data.api2.ApiHelper2
import com.example.turon.data.api2.ApiService2
import com.example.turon.data.api2.models.ControlViewModel
import com.example.turon.data.api2.models.ViewModelFactory
import com.example.turon.databinding.ActivitySecurityBinding
import com.example.turon.utils.SharedPref

class SecurityActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecurityBinding
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragNavHostSecurity) as NavHostFragment).navController
    }
    private val sharedPref by lazy { SharedPref(this) }
    private val model: ControlViewModel by viewModels {
        ViewModelFactory(
            ApiHelper2(
                ApiClient2.createService(
                    ApiService2::class.java,
                    this
                )
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupViews()

        val map = HashMap<String, Any>()
        map["user_id"] = sharedPref.getUserId()
        map["token"] = sharedPref.device_token
        model.sendToken(map)
            .observe(this) {
                if (it.success == true) {
                    Log.d("notify", "loadSplashScreen: succes")
                }
            }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.turnAcceptFragment, R.id.turnAcceptDetailsFragment, R.id.activeTurnFragment,
                R.id.turnHistoryFragment, R.id.activeLoadingFragment, R.id.returnedSecurityFragment,
                R.id.succesHistoryFragment -> {
                    binding.view1.setBackgroundColor(Color.parseColor("#FFCC01"))
                    binding.view2.setBackgroundColor(Color.parseColor("#FF000000"))
                }
                else -> {
                    binding.view1.setBackgroundColor(Color.parseColor("#FF000000"))
                    binding.view2.setBackgroundColor(Color.parseColor("#FFCC01"))
                }
            }
        }
    }

    private fun setupViews() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragNavHostSecurity) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavViewSecurity,
            navHostFragment.navController
        )
    }

    private var backPressedOnce = false
    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id) {
            if (backPressedOnce) {
                super.onBackPressed()
                return
            }

            backPressedOnce = true
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                backPressedOnce = false
            }, 2000)
        } else {
            super.onBackPressed()
        }
    }
}