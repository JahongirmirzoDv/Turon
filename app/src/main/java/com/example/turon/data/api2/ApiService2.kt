package com.example.turon.data.api2

import com.example.turon.data.model.*
import com.example.turon.data.model.response.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService2 {
    @GET("qop_chiqim_tarixi/")
    suspend fun getQop(
        @Query("user_id") user_id: Int,
        @Query("date_start") date_start: String,
        @Query("date_end") date_end: String
    ):FeedQopChiqimHistory

    @GET("get_history_of_tin/")
    suspend fun getBagHistory(
        @Query("user_id") user_id: Int
    ): QopHistoryResponse
}