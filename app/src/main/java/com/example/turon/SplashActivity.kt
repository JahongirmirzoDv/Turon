package com.example.turon

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.turon.auth.AuthActivity
import com.example.turon.data.api2.ApiClient2
import com.example.turon.data.api2.ApiHelper2
import com.example.turon.data.api2.ApiService2
import com.example.turon.data.api2.models.ControlViewModel
import com.example.turon.data.api2.models.ViewModelFactory
import com.example.turon.databinding.ActivitySplashBinding
import com.example.turon.feed.FeedActivity
import com.example.turon.feed_security.FeedSecurity
import com.example.turon.production.ProductionActivity
import com.example.turon.security.SecurityActivity
import com.example.turon.utils.SharedPref
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private var job: Job? = null
    private val sharedPref by lazy { SharedPref(applicationContext) }
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
//        val formatted = current.format(formatter).toString()

        if (sharedPref.deviceDate == "Turon") {
            binding.img.isVisible = false

        } else {
            job = loadSplashScreen()
        }
    }

    private fun loadSplashScreen() = lifecycleScope.launch {
        delay(2000)
        if (isActive) {
            if (sharedPref.isFirstEnter()) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { fm ->
                    if (fm.result != null) {
                        sharedPref.device_token = fm.result.toString()
                    }
                    Log.d("tokenssss", fm.result.toString())
                }
                startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                finishAffinity()

            } else {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { fm ->
                    if (fm.result != null) {
                        sharedPref.device_token = fm.result.toString()
                    }
                    Log.d("tokenssss", fm.result.toString())
                }

//                launch {
//                    val map = HashMap<String, Any>()
//                    map["user_id"] = sharedPref.getUserId()
//                    map["token"] = sharedPref.device_token
//                    model.sendToken(map)
//                        .observe(this@SplashActivity) {
//                            if (it.success == true) {
//                                Log.d("notify", "loadSplashScreen: succes")
//                            }
//                        }
//                }
                when (sharedPref.getUserType()) {
                    "WareHouse" -> {
                        startActivity(Intent(this@SplashActivity, FeedActivity::class.java))
                        finishAffinity()
                    }
                    "Security" -> {
                        startActivity(Intent(this@SplashActivity, SecurityActivity::class.java))
                        finishAffinity()
                    }
                    "FeedSecurity" -> {
                        startActivity(Intent(this@SplashActivity, FeedSecurity::class.java))
                        finishAffinity()
                    }
                    "Scales" -> {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finishAffinity()
                    }
                    "Main_Feed" -> {
                        startActivity(Intent(this@SplashActivity, FeedActivity::class.java))
                        finishAffinity()
                    }
                    "Production" -> {
                        startActivity(Intent(this@SplashActivity, ProductionActivity::class.java))
                        finishAffinity()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}

