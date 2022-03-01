package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.AcceptDetailsData

class AcceptDetailsRepository(private val apiHelper: ApiHelper) {

    suspend fun addWagon(body: HashMap<String, Any>?): UIState<AcceptDetailsData> {
        try {
            val response = apiHelper.addWagon(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error(response.error)
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }
}