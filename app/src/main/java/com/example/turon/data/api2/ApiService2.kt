package com.example.turon.data.api2

import com.example.turon.data.model.FeedQopChiqimHistory
import com.example.turon.data.model.ResponseData
import com.example.turon.data.model.response.QopHistoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService2 {
    @GET("qop_chiqim_tarixi/")
    suspend fun getQop(
        @Query("user_id") user_id: Int,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String
    ): FeedQopChiqimHistory

    @GET("get_history_of_tin/")
    suspend fun getBagHistory(
        @Query("user_id") user_id: Int
    ): QopHistoryResponse

    @POST("return_qop/")
    suspend fun returnBag(
        @Body body: HashMap<String, Any>?
    ): ResponseData

    @POST("edit_store_product/")
    suspend fun Edit(
        @Body body: HashMap<String, Any>?
    ): ResponseData

    @GET("reject_order/")
    suspend fun reject(
        @Query("order_id") order_id: Int
    ): ResponseData


}