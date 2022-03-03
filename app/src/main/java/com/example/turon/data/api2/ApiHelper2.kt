package com.example.turon.data.api2

import com.example.turon.data.model.response.QopHistoryResponse

class ApiHelper2(var apiService2: ApiService2) {
    suspend fun getQop(user_id: Int, date_start: String, date_end: String) =
        apiService2.getQop(user_id, date_start, date_end)

    suspend fun getBagHistory(user_id: Int): QopHistoryResponse = apiService2.getBagHistory(user_id)
}