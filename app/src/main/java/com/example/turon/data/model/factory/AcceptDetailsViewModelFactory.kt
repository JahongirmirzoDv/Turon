package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.AcceptDetailsRepository
import com.example.turon.scales.ui.product_acceptance.AcceptDetailsViewModel

class AcceptDetailsViewModelFactory (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcceptDetailsViewModel::class.java)) {
            return AcceptDetailsViewModel(AcceptDetailsRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}