package com.example.turon.feed.history

import androidx.lifecycle.ViewModel
import androidx.room.FtsOptions
import com.example.turon.data.model.Balance
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.ReturnedBasked
import com.example.turon.data.model.ReturnedSec
import com.example.turon.data.model.repository.FeedAcceptHistoryRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.AddBagExpenseResponse
import com.example.turon.data.model.response.EditStoreResponse

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class FeedAcceptHistoryViewModel(private val repository: FeedAcceptHistoryRepository) :
    ViewModel() {

    private val _brandBalance = MutableStateFlow<UIState<List<Balance>>>(UIState.Loading)
    val brandBalance: StateFlow<UIState<List<Balance>>> = _brandBalance
    suspend fun getBrandBalanceFeed() {
        _brandBalance.value = repository.getBrandBalanceFeed()
    }

    private val _confirmReturnedState = MutableStateFlow<UIState<AddBagExpenseResponse>>(UIState.Loading)
    val confirmReturnedState: StateFlow<UIState<AddBagExpenseResponse>> = _confirmReturnedState
    suspend fun postconfirmReturned(body: HashMap<String, Any>?) {
        _confirmReturnedState.value = repository.confirmReturned(body)
    }


    private val _returnBasked = MutableStateFlow<UIState<List<ReturnedBasked>>>(UIState.Loading)
    val returnBasked: StateFlow<UIState<List<ReturnedBasked>>> = _returnBasked
    suspend fun getReturnedBasked(qaytuv_id: Int, user_id: Int) {
        _returnBasked.value = repository.getReturnedBasked(qaytuv_id, user_id)
    }

 private val _returnSec = MutableStateFlow<UIState<List<ReturnedSec>>>(UIState.Loading)
    val returnSec: StateFlow<UIState<List<ReturnedSec>>> = _returnSec
    suspend fun getReturnedSec() {
        _returnSec.value = repository.getReturnedSec()
    }


}