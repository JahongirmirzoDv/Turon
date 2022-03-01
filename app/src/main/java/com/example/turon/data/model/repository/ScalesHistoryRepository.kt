package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.RequestEditScales
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.AcceptDetailsData
import com.example.turon.data.model.response.EditAktData
import retrofit2.http.Body

class ScalesHistoryRepository(private val apiHelper: ApiHelper) {


    suspend fun getAktWagonAll(akt_id:String): UIState<AcceptDetailsData> {
        try {
            val response = apiHelper.getAktWagonAll(akt_id)
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

    suspend fun editAktHistory(body: RequestEditScales): UIState<EditAktData> {
        try {
            val response = apiHelper.editAktHistory(body)
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