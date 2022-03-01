package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.Acceptance
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.EditStoreResponse
import com.example.turon.data.model.response.FeedAcceptanceData
import com.example.turon.data.model.response.OrderData
import com.example.turon.data.model.response.ProductAcceptData

class FeedAcceptanceRepository(private val apiHelper: ApiHelper) {


    suspend fun getNewAccept(user_id:Int): UIState<List<Acceptance>> {
        try {
            val response = apiHelper.getNewAccept(user_id)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                }
                else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }



    suspend fun postAcceptProduct(store_id:Int): UIState<EditStoreResponse> {
        try {
            val response = apiHelper.postAcceptProduct(store_id)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response)
                }
                else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }
}