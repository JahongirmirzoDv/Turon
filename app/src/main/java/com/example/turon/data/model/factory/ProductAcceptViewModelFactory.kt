package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.ProductAcceptRepository
import com.example.turon.scales.ui.product_acceptance.ProductAcceptViewModel

class ProductAcceptViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductAcceptViewModel::class.java)) {
            return ProductAcceptViewModel(ProductAcceptRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}