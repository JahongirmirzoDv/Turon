package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiService
import com.example.turon.security.viewmodels.TurnHistoryViewModel

class TurnHistoryViewModelFactory (private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TurnHistoryViewModel::class.java)) {
            return TurnHistoryViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}