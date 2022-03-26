package com.example.turon.feed_security

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.turon.R
import com.example.turon.databinding.ActivityFeedSecurityBinding
import com.example.turon.utils.SharedPref

class FeedSecurity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedSecurityBinding
    lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val sharedPref by lazy { SharedPref(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedSecurityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (sharedPref.getUserType() != "Main_Feed") {
            binding.bottomNavViewFeed.menu.removeItem(R.id.commodityAcceptanceFragment2)
        }
        setupViews()
    }

    @SuppressLint("ResourceType")
    private fun setupViews() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragNavHostFeed) as NavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.graph

        if (sharedPref.getUserType() != "Main_Feed") {
            navGraph.startDestination = R.id.sendProductFragment
        } else {
            navGraph.startDestination = R.id.commodityAccepttanceFeedSecurityFragment
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