package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.AuthRepository
import com.example.turon.auth.AuthViewModel

class AuthViewModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(AuthRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}