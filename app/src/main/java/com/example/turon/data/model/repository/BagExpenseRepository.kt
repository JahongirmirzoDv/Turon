package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.BagExpenseHistory
import com.example.turon.data.model.BagRoom
import com.example.turon.data.model.Qoblar
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.AddBagExpenseResponse
import com.example.turon.data.model.response.TegirmonData

class BagExpenseRepository(private val apiHelper: ApiHelper) {
//    suspend fun getQoldiq(userId: Int): List<Qoblar> {
//        var data = listOf<Qoblar>()
//        try {
//            val response = apiHelper.getQoldiq(userId)
//            if (response.isSuccessful) {
//                val response = response.body()!!
//                data = if (response.success){
//                    response.qoblar
//                }else emptyList()
//            }
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
//        return data
//    }

    suspend fun getBagHistory(userId: Int): UIState<List<BagExpenseHistory>> {
        try {
            val response = apiHelper.getBagExpHistory(userId)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.qop_history)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }

    suspend fun addBagExpense(map: HashMap<String, Any>): UIState<AddBagExpenseResponse> {
        try {
            val response = apiHelper.addBagExpense(map)
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


    suspend fun getBagRoom(userId: Int): UIState<List<BagRoom>> {
        try {
            val response = apiHelper.getBagRoom(userId)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.qop)
                } else {
                    UIState.Error("Error")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }


    //getProvider
}