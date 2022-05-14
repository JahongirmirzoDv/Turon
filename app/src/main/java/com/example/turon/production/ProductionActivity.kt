package com.example.turon.production

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.turon.R
import com.example.turon.databinding.ActivityProductionBinding

class ProductionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductionBinding
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragNavHostPro) as NavHostFragment).navController
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupViews()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.acceptanceProFragment -> {
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
            supportFragmentManager.findFragmentById(R.id.fragNavHostPro) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavViewPro,
            navHostFragment.navController
        )
    }

    private var backPressedOnce = false
    override fun onBackPressed() {
        if (navController.graph.startDestinationId == navController.currentDestination?.id) {
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