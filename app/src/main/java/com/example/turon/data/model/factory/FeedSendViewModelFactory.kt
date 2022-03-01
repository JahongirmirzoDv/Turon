package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.FeedAcceptanceRepository
import com.example.turon.data.model.repository.FeedSendProductRepository
import com.example.turon.feed.commodityacceptance.AcceptanceViewModel
import com.example.turon.feed.sendproduct.SendProductViewModel

class FeedSendViewModelFactory (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SendProductViewModel::class.java)) {
            return SendProductViewModel(FeedSendProductRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}