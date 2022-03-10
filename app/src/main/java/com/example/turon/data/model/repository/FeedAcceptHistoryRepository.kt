package com.example.turon.data.model.repository

import android.util.Log
import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.Balance
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.ReturnedBasked
import com.example.turon.data.model.ReturnedSec
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.*

class FeedAcceptHistoryRepository(private val apiHelper: ApiHelper) {

    suspend fun getBrandBalanceFeed(): UIState<List<Balance>> {
        try {
            val response = apiHelper.getBrandBalanceFeed()
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            Log.e("qoldiq", "getBrandBalanceFeed: ${e.message} : ${e.printStackTrace()}")
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }


    suspend fun getReturnedBasked(qaytuv_id:Int,user_id:Int): UIState<List<ReturnedBasked>> {
        try {
            val response = apiHelper.getReturnedBasked(qaytuv_id,user_id)
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

    suspend fun getReturnedSec(): UIState<List<ReturnedSec>> {
        try {
            val response = apiHelper.getReturnedSec()
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



    suspend fun confirmReturned(body: HashMap<String, Any>?): UIState<AddBagExpenseResponse> {
        try {
            val response = apiHelper.confirmReturned(body)
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