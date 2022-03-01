package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.FeedSendProductRepository
import com.example.turon.data.model.repository.TurnAcceptRepository
import com.example.turon.feed.sendproduct.SendProductViewModel
import com.example.turon.security.viewmodels.TurnAcceptViewModel

class TurnAcceptViewModelFactory (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TurnAcceptViewModel::class.java)) {
            return TurnAcceptViewModel(TurnAcceptRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}