package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.api.ApiService
import com.example.turon.production.viewmodels.AllHistoryViewModel

class AllHistoryViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllHistoryViewModel::class.java)) {
            return AllHistoryViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}