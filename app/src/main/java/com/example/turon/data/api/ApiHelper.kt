package com.example.turon.data.api

import com.example.turon.data.model.*
import com.example.turon.data.model.response.HistoryProResponse
import okhttp3.RequestBody
import retrofit2.Response

class ApiHelper(private val apiService: ApiService) {
    suspend fun login(body: HashMap<String, Any>?) = apiService.login(body)
    suspend fun addWagon(body: HashMap<String, Any>?) = apiService.addWagon(body)
    suspend fun confirmReturned(body: HashMap<String, Any>?) = apiService.confirmReturned(body)
    suspend fun postAddStoreFeed(body: HashMap<String, Any>?) = apiService.postAddStoreFeed(body)

    suspend fun addTurn(body: HashMap<String, Any>?) = apiService.addTurn(body)
    suspend fun insertTurns(body: HashMap<String, Any>?) = apiService.insertTurns(body)
    suspend fun addBagExpense(body: HashMap<String, Any>?) = apiService.addBagExpense(body)
    suspend fun addBagInCome(body: HashMap<String, Any>?) = apiService.addBagInCome(body)

    suspend fun carLeave(turnId: Int, status: Int) = apiService.carLeave(turnId, status)
    suspend fun getActiveAkt() = apiService.getActiveAkt()

    ///Feed and Flour for active order
    suspend fun getOrder(user_id: Int) = apiService.getOrder(user_id)

    suspend fun getTurnAccept(user_id: Int) = apiService.getTurnAccept(user_id)
    suspend fun getTurnHistory() = apiService.getTurnHistory()

    suspend fun getActiveTurn() = apiService.getActiveTurn()
    suspend fun getReturnedGoodsClient(orderId: Int, userId: Int) =
        apiService.getReturnedGoodsClient(orderId, userId)

    suspend fun getTurnClient(order_id: Int) = apiService.getTurnClient(order_id)
    suspend fun postReturnedGoods(map: RequestBody) = apiService.postReturnedGoods(map)
    suspend fun getNewAccept(user_id: Int) = apiService.getNewAccept(user_id)
    suspend fun postAcceptProduct(store_id: Int) = apiService.postAcceptProduct(store_id)

    //del
    suspend fun postItemPro(code: RequestProC) = apiService.postItemPro(code)
    suspend fun postReturnProduct(code: RequestReturnProduct) = apiService.postReturnProduct(code)

    suspend fun sendOrderFinal(body: RequestBody) = apiService.sendOrderFinal(body)

    suspend fun postLoadOrder(code: RequestPro) = apiService.postLoadOrder(code)
    suspend fun addCargoToBasket(bask_id: Int, brigada: Int) =
        apiService.addCargoToBasket(bask_id, brigada)

    suspend fun getProductPro(user_id: Int) = apiService.getProductPro(user_id)
    suspend fun getReturnedGoods(user_id: Int) = apiService.getReturnedGoods(user_id)

    suspend fun getReturnedBasket(user_id: Int) = apiService.getReturnedGoods(user_id)
    suspend fun getOrderFeedHistory(user_id: Int) = apiService.getOrderFeedHistory(user_id)

    suspend fun getCargoMan() = apiService.getCargoMan()

    suspend fun getProvider(userId: Int) = apiService.getProvider(userId)
    suspend fun getTypeTin(userId: Int) = apiService.getTypeTin(userId)
    suspend fun getFilterTin(request: InComeRequest) = apiService.getFilterTin(
        request.qop_turi,
        request.kun,
        request.hafta,
        request.oy,
        request.start,
        request.end,
        request.user_id
    )

    suspend fun getBagHistory(userId: Int) = apiService.getBagHistory(userId)
    suspend fun getBagExpHistory(userId: Int) = apiService.getBagExpHistory(userId)
    suspend fun getBagRoom(userId: Int) = apiService.getBagRoom(userId)
    suspend fun getQoldiq(user_id: Int) = apiService.getQoldiq(user_id)


    suspend fun getOrderDetails(user_id: Int, order_id: Int) =
        apiService.getOrderDetails(user_id, order_id)

    suspend fun getReturnedBasket(user_id: Int, qaytuv_id: Int) =
        apiService.getReturnedBasket(user_id, qaytuv_id)

    suspend fun getLoadOrder(user_id: Int, order_id: Int) =
        apiService.getLoadOrder(user_id, order_id)

    suspend fun getReturnedBasked(qaytuv_id: Int, order_id: Int) =
        apiService.getReturnedBasked(qaytuv_id, order_id)

    suspend fun getReturnedSec() =
        apiService.getReturnedSec()

    suspend fun getBrandBalanceFeed() = apiService.getBrandBalanceFeed()

    suspend fun postEditStore(body: HashMap<String, Any>?) = apiService.postEditStore(body)
    suspend fun getHistoryAkt() = apiService.getHistoryAkt()
    suspend fun getHistoryAktFilter(date_start: String, date_end: String) =
        apiService.getHistoryAktFilter(date_start, date_end)

    suspend fun getAktWagonAll(akt_id: String) = apiService.getAktWagonAll(akt_id)
    suspend fun editAktHistory(body: RequestEditScales) = apiService.editAktHistory(body)

    suspend fun getHistoryProFilter(
        user_id: Int,
        date_start: String,
        date_end: String
    ): HistoryProResponse = apiService.getHistoryProFilter(user_id,date_start, date_end)

}