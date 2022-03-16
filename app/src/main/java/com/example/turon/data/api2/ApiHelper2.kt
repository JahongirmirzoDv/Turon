package com.example.turon.data.api2

import com.example.turon.data.model.ChiqmdanQaytaglar
import com.example.turon.data.model.QopChiqimhistory2
import com.example.turon.data.model.ResponseData
import com.example.turon.data.model.response.QopHistoryResponse

class ApiHelper2(var apiService2: ApiService2) {
    suspend fun getQop(user_id: Int, from_date: String, to_date: String) =
        apiService2.getQop(user_id, from_date, to_date)

    suspend fun getBagHistory(user_id: Int,date_start: String,date_end: String): QopHistoryResponse = apiService2.getBagHistory(user_id,date_start,date_end)
    suspend fun getreturnedincome(user_id: Int,date_start:String,date_end:String): QopChiqimhistory2 = apiService2.getreturnedincome(user_id,date_start,date_end)

    suspend fun returnBag(map: HashMap<String, Any>): ResponseData = apiService2.returnBag(map)

    suspend fun returnExpanceQop(map: HashMap<String, Any>): ResponseData = apiService2.returnExpanceQop(map)

    suspend fun returnIncomeQop(map: HashMap<String, Any>): ResponseData = apiService2.returnIncomeQop(map)

    suspend fun Edit(map: HashMap<String, Any>?): ResponseData = apiService2.Edit(map)

    suspend fun reject(order_id: Int): ResponseData = apiService2.reject(order_id)
    suspend fun chiqimdanQaytarilganlar(user_id: Int): ChiqmdanQaytaglar = apiService2.chiqimdanQaytarilganlar(user_id)

    suspend fun reject_turn(map: HashMap<String, Any>?) = apiService2.reject_turn(map)

    suspend fun crrete_clinet_tin(map: HashMap<String, Any>?):ResponseData = apiService2.crrete_clinet_tin(map)
}