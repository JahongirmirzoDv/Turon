package com.example.turon.feed.sendproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.turon.data.model.OrderBasked
import com.example.turon.data.model.repository.FeedSendProductRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.EditStoreResponse
import com.example.turon.data.model.response.OrderData
import com.example.turon.data.model.response.OrderDetailsData
import com.example.turon.data.model.response.TegirmonData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.RequestBody

class SendProductViewModel(private val repository: FeedSendProductRepository) : ViewModel() {



    private val _orderState = MutableStateFlow<UIState<List<OrderData>>>(UIState.Loading)
    val orderState: StateFlow<UIState<List<OrderData>>> = _orderState
    suspend fun getOrder(user_id:Int) {
        _orderState.value = repository.getOrder(user_id)
    }

    private val _orderFinalState = MutableStateFlow<UIState<EditStoreResponse>>(UIState.Loading)
    val orderFinalState: StateFlow<UIState<EditStoreResponse>> = _orderFinalState
    suspend fun sendOrderFinal(body:RequestBody) {
        _orderFinalState.value = repository.sendOrderFinal(body)
    }


    private val _orderDetailsState =
        MutableStateFlow<UIState<List<OrderBasked>>>(UIState.Loading)
    val orderDetailsState: StateFlow<UIState<List<OrderBasked>>> = _orderDetailsState
    suspend fun getOrderDetails(user_id: Int,order_id:Int) {
        _orderDetailsState.value = repository.getOrderDetails(user_id,order_id)
    }

    private val _caroManState = MutableStateFlow<UIState<List<TegirmonData>>>(UIState.Loading)
    val caroManState: StateFlow<UIState<List<TegirmonData>>> = _caroManState
    suspend fun getCargoMan() {
        _caroManState.value = repository.getCargoMan()
    }
    private val _orderHistory= MutableStateFlow<UIState<List<OrderData>>>(UIState.Loading)
    val orderHistory: StateFlow<UIState<List<OrderData>>> = _orderHistory
    suspend fun getOrderFeedHistory(user_id: Int) {
        _orderHistory.value = repository.getOrderFeedHistory(user_id)
    }


    private var orderList = ArrayList<OrderDetailsData>()
    fun sendOrderDetail(arrayList: ArrayList<OrderDetailsData>) {
        orderList = arrayList
    }
    fun getSendOrderDetail():MutableLiveData<ArrayList<OrderDetailsData>>{
        return MutableLiveData(orderList)
    }
}