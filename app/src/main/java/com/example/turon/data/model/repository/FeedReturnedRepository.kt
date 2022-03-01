package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.OrderBasked
import com.example.turon.data.model.ReturnBasket
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.EditStoreResponse
import com.example.turon.data.model.response.OrderData
import com.example.turon.data.model.response.OrderDetailsData
import com.example.turon.data.model.response.TegirmonData
import okhttp3.RequestBody

class FeedReturnedRepository(private val apiHelper: ApiHelper) {


    suspend fun getReturnedGoods(user_id:Int): UIState<List<OrderData>> {
        try {
            val response = apiHelper.getReturnedGoods(user_id)
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

    suspend fun getReturnedBasket(user_id:Int,qaytuv:Int): UIState<List<ReturnBasket>> {
        try {
            val response = apiHelper.getReturnedBasket(user_id,qaytuv)
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

    suspend fun postReturnedGoods(map: RequestBody): UIState<EditStoreResponse> {
        try {
            val response = apiHelper.postReturnedGoods(map)
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
}