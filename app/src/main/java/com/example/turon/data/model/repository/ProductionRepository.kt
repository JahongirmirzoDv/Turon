package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.*
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.EditStoreResponse
import com.example.turon.data.model.response.FeedAcceptanceData
import com.example.turon.data.model.response.ProAcceptResponse
import com.example.turon.data.model.response.TegirmonData

class ProductionRepository(private val apiHelper: ApiHelper)  {

    suspend fun getProductPro(user_id: Int): UIState<List<ProductPro>> {
        try {
            val response = apiHelper.getProductPro(user_id)
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


    suspend fun getCargoMan(): UIState<List<TegirmonData>> {
        try {
            val response = apiHelper.getCargoMan()
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



    suspend fun getLoadOrder(user_id: Int, order_id: Int): UIState<List<LoadOrder>> {
        try {
            val response = apiHelper.getLoadOrder(user_id,order_id)
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


    suspend fun postItemPro(code: RequestProC): UIState<ProAcceptResponse> {
        try {
            val response = apiHelper.postItemPro(code)
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

    suspend fun postReturnProduct(code: RequestReturnProduct): UIState<EditStoreResponse> {
        try {
            val response = apiHelper.postReturnProduct(code)
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

    suspend fun postLoadOrder(code: RequestPro): UIState<LoadOrderBaskedResponse> {
        try {
            val response = apiHelper.postLoadOrder(code)
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

    suspend fun addCargoToBasket(bask_id:Int,brigada:Int): UIState<EditStoreResponse> {
        try {
            val response = apiHelper.addCargoToBasket(bask_id,brigada)
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