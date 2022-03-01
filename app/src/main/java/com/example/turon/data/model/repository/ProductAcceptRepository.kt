package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.ProductAcceptData

class ProductAcceptRepository(private val apiHelper: ApiHelper) {


    suspend fun getActiveAkt(): UIState<List<ProductAcceptData>> {
        try {
            val response = apiHelper.getActiveAkt()
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

    suspend fun getHistoryAkt(): UIState<List<ProductAcceptData>> {
        try {
            val response = apiHelper.getHistoryAkt()
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

    suspend fun getHistoryAktFilter(date_start: String,date_end: String): UIState<List<ProductAcceptData>> {
        try {
            val response = apiHelper.getHistoryAktFilter(date_start,date_end)
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

}