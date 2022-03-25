package com.example.turon.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.turon.MainActivity
import com.example.turon.data.api.ApiClient
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.data.model.factory.AuthViewModelFactory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.databinding.ActivityAuthBinding
import com.example.turon.feed.FeedActivity
import com.example.turon.production.ProductionActivity
import com.example.turon.security.SecurityActivity
import com.example.turon.utils.SharedPref
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var progressDialog: AlertDialog
    private val sharedPref by lazy { SharedPref(applicationContext) }
    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            ApiHelper(ApiClient.createService(ApiService::class.java, this))
        )
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.submitBtn.setOnClickListener {
            loginRequest(binding.password.text.toString(), binding.phone.text.toString())
        }
        setupUI()
    }

    private fun setupUI() {
        progressDialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Yuklanmoqda")
            .setCancelable(false)
            .build()
    }

    @InternalCoroutinesApi
    private fun loginRequest(password: String, userName: String) {
        progressDialog.show()
        val map = HashMap<String, Any>()
        map["password"] = password
        map["username"] = userName

        lifecycleScope.launchWhenStarted {
            viewModel.login(map)
            viewModel.loginState.collect {
                when (it) {
                    is UIState.Success -> {
                        progressDialog.dismiss()
                        sharedPref.setUserId(it.data.id)
                        sharedPref.setFirstEnter(false)
                        when (it.data.type) {
                            15, 16 -> {
                                sharedPref.setUserType("WareHouse")
                                startActivity(Intent(this@AuthActivity, FeedActivity::class.java))
                                finishAffinity()
                            }
                            5 -> {
                                sharedPref.setUserType("Security")
                                startActivity(
                                    Intent(
                                        this@AuthActivity,
                                        SecurityActivity::class.java
                                    )
                                )
                                finishAffinity()
                            }
                            6 -> {
                                sharedPref.setUserType("Main_Feed")
                                startActivity(Intent(this@AuthActivity, FeedActivity::class.java))
                                finishAffinity()
                            }
                            7 -> {
                                sharedPref.setUserType("Scales")
                                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                                finishAffinity()
                            }
                            13, 14 -> {
                                sharedPref.setUserType("Production")
                                startActivity(
                                    Intent(
                                        this@AuthActivity,
                                        ProductionActivity::class.java
                                    )
                                )
                                finishAffinity()
                            }
                        }
                    }
                    is UIState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Loading -> {

                    }
                    else -> Unit
                }
            }
        }
    }
}