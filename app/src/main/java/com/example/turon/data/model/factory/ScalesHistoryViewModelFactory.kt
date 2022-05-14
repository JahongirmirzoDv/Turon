package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.ScalesHistoryRepository
import com.example.turon.scales.ui.histroy.ScalesHistoryViewModel

class ScalesHistoryViewModelFactory (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScalesHistoryViewModel::class.java)) {
            return ScalesHistoryViewModel(ScalesHistoryRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }


}