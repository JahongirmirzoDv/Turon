package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.BagExpenseRepository
import com.example.turon.data.model.repository.TurnAcceptRepository
import com.example.turon.security.viewmodels.BagExpenseViewModel
import com.example.turon.security.viewmodels.TurnAcceptViewModel

class BagExpenseViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BagExpenseViewModel::class.java)) {
            return BagExpenseViewModel(BagExpenseRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}