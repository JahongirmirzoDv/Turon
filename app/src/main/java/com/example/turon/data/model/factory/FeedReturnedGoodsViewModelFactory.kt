package com.example.turon.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.FeedReturnedRepository
import com.example.turon.feed.returnedgoods.ReturnedGoodsViewModel

class FeedReturnedGoodsViewModelFactory (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReturnedGoodsViewModel::class.java)) {
            return ReturnedGoodsViewModel(FeedReturnedRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }


}