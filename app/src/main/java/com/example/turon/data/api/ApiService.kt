package com.example.turon.data.api

import com.example.turon.data.model.*
import com.example.turon.data.model.response.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login/")
    suspend fun login(
        @Body() body: HashMap<String, Any>?
    ): Response<LoginResponse>

    @POST("add_turn/")
    suspend fun addTurn(
        @Body() body: HashMap<String, Any>?
    ): Response<EditStoreResponse>

    @POST("enter_car/")
    suspend fun insertTurns(
        @Body() body: HashMap<String, Any>?
    ): Response<EditStoreResponse>

    @POST("expance_qop/")
    suspend fun addBagExpense(
        @Body() body: HashMap<String, Any>?
    ): Response<AddBagExpenseResponse>

    @POST("create_tin/")
    suspend fun addBagInCome(
        @Body() body: HashMap<String, Any>?
    ): Response<AddBagExpenseResponse>

    @FormUrlEncoded
    @POST("car_leave/")
    suspend fun carLeave(
        @Field("turn_id") turn_id: Int,
        @Field("status") status: Int
    ): Response<EditStoreResponse>

    @POST("edit_store/")
    suspend fun postEditStore(
        @Body() body: HashMap<String, Any>?
    ): Response<EditStoreResponse>

    @POST("add_store/")
    suspend fun postAddStoreFeed(
        @Body() body: HashMap<String, Any>?
    ): Response<EditStoreResponse>

    ///qaytgan tavarlar ro`yxati
    @GET("get_qaytuv/")
    suspend fun getReturnedGoods(@Query("user_id") user_id: Int): Response<ActiveOrderResponse>

    //galib ishliman
    @GET("get_qaytuv_basket/")
    suspend fun getReturnedBasket(
        @Query("user_id") user_id: Int,
        @Query("qaytuv_id") qaytuv_id: Int
    ): Response<ReturnBasketResponse>

    @GET("get_basket_forsecurity/")
    suspend fun getTurnClient(
        @Query("order_id") order_id: Int
    ): Response<TurnDetailsResponse>

    @GET("store/securitysended/")
    suspend fun getTurnHistory(
        @Query("page") page: Int,
        @Query("user_id") user_id: Int,
        @Query("item") item: String
    ): TurnHistoryResponse

    @GET("store/securitysendedtashkent/")
    suspend fun getTurnHistoryTo(
        @Query("page") page: Int,
        @Query("user_id") user_id: Int,
        @Query("item") item: String
    ): TurnHistoryResponse

    @GET("get_loading_turn/")
    suspend fun getActiveTurn(@Query("user_id") user_id: Int): Response<ActiveTurnResponse>

    @GET("get_passive_turn/")
    suspend fun getActiveTurn2(@Query("user_id") user_id: Int): Response<ActiveTurnResponse>

    ///active order
    @GET("get_order/")
    suspend fun getOrder(@Query("user_id") user_id: Int): Response<ActiveOrderResponse>

    @GET("get_order_to_give_turn/")
    suspend fun getTurnAccept(
        @Query("user_id") user_id: Int
    ): Response<TurnResponse>

    @GET("get_active_turn/")
    suspend fun getTurnHistory(@Query("user_id") user_id: Int): Response<TurnHistoryActInAct>

    @GET("get_qaytuv_basket/")
    suspend fun getReturnedGoodsClient(
        @Query("qaytuv_id") qaytuv_id: Int,
        @Query("user_id") user_id: Int
    ): Response<OrderDetailsResponse>

    @POST("load_qaytuv_basket/")
    suspend fun postReturnedGoods(@Body map: RequestBody): Response<EditStoreResponse>

    @GET("get_basket/")
    suspend fun getOrderDetails(
        @Query("user_id") user_id: Int,
        @Query("order_id") order_id: Int
    ): Response<OrderDetailsResponse>

    @GET("get_basket_product/")
    suspend fun getLoadOrder(
        @Query("user_id") user_id: Int,
        @Query("order_id") order_id: Int
    ): Response<LoadOrderResponse>

    @GET("get_load_qaytuv_basket/")
    suspend fun getReturnedBasked(
        @Query("user_id") user_id: Int,
        @Query("qaytuv_id") qaytuv_id: Int
    ): Response<ReturnedBaskedResponse>

    @GET("returnedproducts/")
    suspend fun getReturnedSec(
        @Query("user_id") user_id: Int
    ): Response<ReturnedSecResponse>

    @GET("get_sended_order/")
    suspend fun getOrderFeedHistory(
        @Query("user_id") user_id: Int
    ): Response<ActiveOrderResponse>

    @GET("get_active_akt/")
    suspend fun getActiveAkt(): Response<ProductAcceptResponse>

    @GET("get_brigada/")
    suspend fun getCargoMan(): Response<CargoManResponse>

    @GET("get_clients_qop/")
    suspend fun getProvider(
        @Query("user_id") user_id: Int
    ): Response<ProvidersResponse>

    @GET("get_type_of_tin/")
    suspend fun getTypeTin(
        @Query("user_id") user_id: Int
    ): Response<TypeOfTinResponse>

    @GET("filter_statistics/")
    suspend fun getFilterTin(
        @Query("qop_turi") qop_turi: String,
        @Query("kun") kun: String,
        @Query("hafta") hafta: String,
        @Query("oy") oy: String,
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("user_id") user_id: Int,
    ): Response<FilterOfTinResponse>

    @GET("get_history_of_tin/")
    suspend fun getBagHistory(
        @Query("user_id") user_id: Int
    ): Response<QopHistoryResponse>

    @GET("get_tin_qoldiq/")
    suspend fun getQoldiq(
        @Query("user_id") user_id: Int
    ): Response<QoldiqData>

    @GET("qop_chiqim_tarixi/")
    suspend fun getFeedQopChiqimHistory(
        @Query("user_id") user_id: Int
    ): Response<FeedQopChiqimHistory>

    @GET("get_history_of_tin/")
    suspend fun getBagExpHistory(
        @Query("user_id") user_id: Int
    ): Response<BagExpenseHistoryResponse>

    @GET("get_qop_ombor/")
    suspend fun getBagRoom(@Query("user_id") user_id: Int): Response<BagRoomResponse>

    @GET("store/storehistory/accepted/")
    suspend fun getFeedAcceptHistory(
        @Query("page") page: Int,
        @Query("user_id") user_id: Int
    ): HistoryProResponse

    @GET("store/qaytuv/")
    suspend fun getReturnedHistory(
        @Query("page") page: Int,
        @Query("user_id") user_id: Int
    ): OrderHistoryResponse

    @GET("store/returned_products/")
    suspend fun getReturnedProHistory(
        @Query("page") page: Int,
        @Query("user_id") user_id: Int
    ): HistoryProResponse

    @GET("store/order/")
    suspend fun getOrderHistory(
        @Query("page") page: Int,
        @Query("user_id") user_id: Int,
        @Query("item") item: String,
        @Query("from_date") date_start: String,
        @Query("to_date") date_end: String
    ): OrderHistoryResponse

    @GET("get_store_product/")
    suspend fun getBrandBalanceFeed(
        @Query("user_id") user_id: Int
    ): Response<BalanceResponse>

    @GET("get_akt/")
    suspend fun getHistoryAkt(): Response<ProductAcceptResponse>

    @GET("get_store_history/")
    suspend fun getNewAccept(
        @Query("user_id") user_id: Int
    ): Response<AcceptanceResponse>

    @FormUrlEncoded
    @POST("changestatusofhistory/")
    suspend fun postAcceptProduct(
        @Field("store_id") store_id: Int
    ): Response<EditStoreResponse>

    @GET("store/productionhistory/")
    suspend fun getHistoryPro(
        @Query("page") page: Int,
        @Query("user_id") user_id: Int
    ): HistoryProResponse

    @GET("store/productionhistory/")
    suspend fun getHistoryProFilter(
        @Query("user_id") user_id: Int,
        @Query("date_start") date_start: String,
        @Query("date_end") date_end: String
    ): HistoryProResponse

    @GET("store/productionhistory/")
    fun getQopChiqim(
        @Query("user_id") user_id: Int,
        @Query("date_start") date_start: String,
        @Query("date_end") date_end: String
    ): FeedQopChiqimHistory

    @GET("get_product/")
    suspend fun getProductPro(
        @Query("user_id") user_id: Int
    ): Response<ProductResponse>

    @POST("add_store/")
    suspend fun postItemPro(
        @Body() body: RequestProC
    ): Response<ProAcceptResponse>

    @POST("return_product/")
    suspend fun postReturnProduct(
        @Body() body: RequestReturnProduct
    ): Response<EditStoreResponse>

    @POST("load_order_basket/")
    suspend fun postLoadOrder(
        @Body() body: RequestPro
    ): Response<LoadOrderBaskedResponse>

    @FormUrlEncoded
    @POST("add_brigada_tobasket/")
    suspend fun addCargoToBasket(
        @Field("bask_id") bask_id: Int,
        @Field("brigada") brigada: Int
    ): Response<EditStoreResponse>

    @POST("send_order/")
    suspend fun sendOrderFinal(
        @Body() body: RequestBody
    ): Response<EditStoreResponse>

    @GET("get_akt/")
    suspend fun getHistoryAktFilter(
        @Query("date_start") date_start: String,
        @Query("date_end") date_end: String
    ): Response<ProductAcceptResponse>

    @GET("get_akt_wagon_all/")
    suspend fun getAktWagonAll(@Query("akt_id") akt_id: String): Response<AcceptDetailsResponse>

    @POST("add_wagon/")
    suspend fun addWagon(@Body() body: HashMap<String, Any>?): Response<AcceptDetailsResponse>

    @POST("confirmreturned/")
    suspend fun confirmReturned(@Body() body: HashMap<String, Any>?): Response<AddBagExpenseResponse>

    @POST("edit-wagon/")
    suspend fun editAktHistory(@Body() body: RequestEditScales): Response<EditAktResponse>

}