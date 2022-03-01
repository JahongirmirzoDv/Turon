package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.InComeRequest
import com.example.turon.data.model.QopHistory
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.AddBagExpenseResponse
import com.example.turon.data.model.response.TegirmonData
import java.util.HashMap

class BagInComeRepository(private val apiHelper: ApiHelper) {



    suspend fun addBagInCome(map: HashMap<String, Any>): UIState<AddBagExpenseResponse> {
        try {
            val response = apiHelper.addBagInCome(map)
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


    suspend fun getProvider(userId: Int): UIState<List<TegirmonData>> {
        try {
            val response = apiHelper.getProvider(userId)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success)
                UIState.Success(response.clients)
                else{
                    return UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }


    suspend fun getTypeTin(userId: Int): UIState<List<TegirmonData>> {
        try {
            val response = apiHelper.getTypeTin(userId)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success)
                    UIState.Success(response.qop_history)
                else {
                    return UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }




    suspend fun getBagHistory(userId: Int): UIState<List<QopHistory>> {
        try {
            val response = apiHelper.getBagHistory(userId)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.qop_history)
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


    suspend fun getFilterTin(request: InComeRequest): UIState<List<QopHistory>> {
        try {
            val response = apiHelper.getFilterTin(request)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.filtered)
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