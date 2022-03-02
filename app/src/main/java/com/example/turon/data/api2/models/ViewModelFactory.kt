package com.example.turon.data.api2.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api2.ApiHelper2

class ViewModelFactory(private val apiHelper: ApiHelper2) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ControlViewModel::class.java)) {
            return ControlViewModel(apiHelper) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}