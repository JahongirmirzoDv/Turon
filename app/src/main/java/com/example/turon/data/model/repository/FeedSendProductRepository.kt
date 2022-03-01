package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.OrderBasked
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.*
import okhttp3.RequestBody

class FeedSendProductRepository (private val apiHelper: ApiHelper) {

    suspend fun getOrder(user_id:Int): UIState<List<OrderData>> {
        try {
            val response = apiHelper.getOrder(user_id)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }

    suspend fun sendOrderFinal(body: RequestBody): UIState<EditStoreResponse> {
        try {
            val response = apiHelper.sendOrderFinal(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }



    suspend fun getOrderDetails(user_id: Int,order_id:Int): UIState<List<OrderBasked>> {
        try {
            val response = apiHelper.getOrderDetails(user_id,order_id)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.orderDetailsData)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }


    suspend fun getCargoMan(): UIState<List<TegirmonData>> {
        try {
            val response = apiHelper.getCargoMan()
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }

    suspend fun getOrderFeedHistory(user_id:Int): UIState<List<OrderData>> {
        try {
            val response = apiHelper.getOrderFeedHistory(user_id)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }
}