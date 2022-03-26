package com.example.turon.data.api2

import com.example.turon.data.model.ChiqmdanQaytaglar
import com.example.turon.data.model.FeedQopChiqimHistory
import com.example.turon.data.model.QopChiqimhistory2
import com.example.turon.data.model.ResponseData
import com.example.turon.data.model.response.EditStoreResponse
import com.example.turon.data.model.response.QopHistoryResponse
import retrofit2.Response
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
        @Query("user_id") user_id: Int,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String
    ): QopHistoryResponse

    @GET("get_returned_income_qop/")
    suspend fun getreturnedincome(
        @Query("user_id") user_id: Int,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String
    ): QopChiqimhistory2

    @POST("expance_qop/")
    suspend fun returnBag(
        @Body body: HashMap<String, Any>?
    ): ResponseData

    @POST("return_expanse_qop/")
    suspend fun returnExpanceQop(
        @Body body: HashMap<String, Any>?
    ): ResponseData

    @GET("get_returned_expanse_qop/")
    suspend fun chiqimdanQaytarilganlar(
        @Query("user_id") user_id: Int
    ): ChiqmdanQaytaglar


    @POST("return_income_qop/")
    suspend fun returnIncomeQop(
        @Body body: HashMap<String, Any>?
    ): ResponseData

    @POST("edit_store_product/")
    suspend fun Edit(
        @Body body: HashMap<String, Any>?
    ): ResponseData

    @POST("reject_turned_order/")
    suspend fun reject_turn(
        @Body body: HashMap<String, Any>?
    ): ResponseData

    @GET("reject_order/")
    suspend fun reject(
        @Query("order_id") order_id: Int
    ): ResponseData

    @POST("create_client_tin/")
    suspend fun crrete_clinet_tin(
        @Body body: HashMap<String, Any>?
    ): ResponseData

    @POST("add_turn_and_enter_car/")
    suspend fun addTurn(
        @Body() body: HashMap<String, Any>?
    ): ResponseData
}