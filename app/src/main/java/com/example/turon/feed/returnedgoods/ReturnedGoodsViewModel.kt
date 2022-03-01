package com.example.turon.feed.returnedgoods

import androidx.lifecycle.ViewModel
import com.example.turon.data.model.OrderBasked
import com.example.turon.data.model.ReturnBasket
import com.example.turon.data.model.repository.FeedReturnedRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.EditStoreResponse
import com.example.turon.data.model.response.OrderData
import com.example.turon.data.model.response.OrderDetailsData
import com.example.turon.data.model.response.TegirmonData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.RequestBody

class ReturnedGoodsViewModel(val repository: FeedReturnedRepository) : ViewModel() {


    private val _returnedGoodsState = MutableStateFlow<UIState<List<OrderData>>>(UIState.Loading)
    val returnedGoodsState: StateFlow<UIState<List<OrderData>>> = _returnedGoodsState
    suspend fun getReturnedGoods(user_id: Int) {
        _returnedGoodsState.value = repository.getReturnedGoods(user_id)
    }

    private val _returnedBasketState = MutableStateFlow<UIState<List<ReturnBasket>>>(UIState.Loading)
    val returnedBasketState: StateFlow<UIState<List<ReturnBasket>>> = _returnedBasketState
    suspend fun getReturnedBasket(user_id:Int,qaytuv:Int) {
        _returnedBasketState.value = repository.getReturnedBasket(user_id,qaytuv)
    }


    private val _cargoManState = MutableStateFlow<UIState<List<TegirmonData>>>(UIState.Loading)
    val cargoManState: StateFlow<UIState<List<TegirmonData>>> = _cargoManState
    suspend fun getCargoMan() {
        _cargoManState.value = repository.getCargoMan()
    }


    private val _postReturnedGoodsState =
        MutableStateFlow<UIState<EditStoreResponse>>(UIState.Loading)
    val postReturnedGoodsState: StateFlow<UIState<EditStoreResponse>> = _postReturnedGoodsState

    suspend fun postReturnedGoods(map: RequestBody) {
        _postReturnedGoodsState.value = repository.postReturnedGoods(map)
    }

}