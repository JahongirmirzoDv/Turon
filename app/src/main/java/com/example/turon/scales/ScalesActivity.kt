package com.example.turon.scales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.turon.R
import com.example.turon.databinding.ActivityScalesBinding

class ScalesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScalesBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScalesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupViews()

    }

    private fun setupViews()
    {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragNavHostScales) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavViewScales, navHostFragment.navController)


    }

    private var backPressedOnce = false
    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id)
        {
            if (backPressedOnce)
            {
                super.onBackPressed()
                return
            }

            backPressedOnce = true
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                backPressedOnce = false
            }, 2000)
        }
        else {
            super.onBackPressed()
        }
    }

}