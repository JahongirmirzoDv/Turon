package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.BagExpenseRepository
import com.example.turon.data.model.repository.BagInComeRepository
import com.example.turon.security.viewmodels.BagExpenseViewModel
import com.example.turon.security.viewmodels.BagInComeViewModel

class BagInComeViewModelFactory (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BagInComeViewModel::class.java)) {
            return BagInComeViewModel(BagInComeRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}