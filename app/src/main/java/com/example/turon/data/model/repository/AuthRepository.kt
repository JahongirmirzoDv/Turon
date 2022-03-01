package com.example.turon.data.model.repository

import com.example.turon.data.api.ApiHelper
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.Data
import com.example.turon.data.model.response.OrderDetailsData

class AuthRepository(private val apiHelper: ApiHelper) {

    suspend fun login(body: HashMap<String, Any>?): UIState<Data> {
        try {
            val response = apiHelper.login(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error("Bunday foydalanuvchi mavjud emas!!!")
                }
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }

}