package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.FeedAcceptanceRepository
import com.example.turon.data.model.repository.ProductAcceptRepository
import com.example.turon.feed.commodityacceptance.AcceptanceViewModel
import com.example.turon.scales.ui.product_acceptance.ProductAcceptViewModel

class FeedAcceptanceViewModelFactory (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcceptanceViewModel::class.java)) {
            return AcceptanceViewModel(FeedAcceptanceRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}