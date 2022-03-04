package com.example.turon.data.api2

import com.example.turon.data.model.ResponseData
import com.example.turon.data.model.response.QopHistoryResponse

class ApiHelper2(var apiService2: ApiService2) {
    suspend fun getQop(user_id: Int, from_date: String, to_date: String) =
        apiService2.getQop(user_id, from_date, to_date)

    suspend fun getBagHistory(user_id: Int): QopHistoryResponse = apiService2.getBagHistory(user_id)

    suspend fun returnBag(map: HashMap<String, Any>):ResponseData = apiService2.returnBag(map)
}