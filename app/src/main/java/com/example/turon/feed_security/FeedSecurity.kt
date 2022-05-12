package com.example.turon.feed_security

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.turon.R
import com.example.turon.data.api2.ApiClient2
import com.example.turon.data.api2.ApiHelper2
import com.example.turon.data.api2.ApiService2
import com.example.turon.data.api2.models.ControlViewModel
import com.example.turon.data.api2.models.ViewModelFactory
import com.example.turon.databinding.ActivityFeedSecurityBinding
import com.example.turon.utils.SharedPref

class FeedSecurity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedSecurityBinding
    lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
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

    private val sharedPref by lazy { SharedPref(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedSecurityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (sharedPref.getUserType() != "Main_Feed") {
            binding.bottomNavViewFeed.menu.removeItem(R.id.commodityAcceptanceFragment2)
            binding.bottomNavViewFeed.menu.removeItem(R.id.bagIncomeFragment)
        }
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
    }

    @SuppressLint("ResourceType")
    private fun setupViews() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragNavHostFeed) as NavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.graph

        if (sharedPref.getUserType() != "Main_Feed") {
            navGraph.startDestination = R.id.turnAcceptFragment2
        } else {
            navGraph.startDestination = R.id.turnAcceptFragment2
        }
        navController.graph = navGraph

        NavigationUI.setupWithNavController(
            binding.bottomNavViewFeed,
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

            Toast.makeText(this, "Chiqish uchun yana BACK tugmasini bosing", Toast.LENGTH_SHORT)
                .show()

            Handler(Looper.getMainLooper()).postDelayed({
                backPressedOnce = false
            }, 2000)
        } else {
            super.onBackPressed()
        }
    }
}