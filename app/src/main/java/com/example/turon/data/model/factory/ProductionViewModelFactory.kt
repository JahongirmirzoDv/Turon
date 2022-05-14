package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.FeedAcceptanceRepository
import com.example.turon.data.model.repository.ProductionRepository
import com.example.turon.feed.commodityacceptance.AcceptanceViewModel
import com.example.turon.production.viewmodels.ProductionViewModel

class ProductionViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductionViewModel::class.java)) {
            return ProductionViewModel(ProductionRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }


}