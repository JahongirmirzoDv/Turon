package com.example.turon.feed.commodityacceptance

import androidx.lifecycle.ViewModel
import com.example.turon.data.model.Acceptance
import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.repository.FeedAcceptanceRepository
import com.example.turon.data.model.repository.ProductAcceptRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.EditStoreResponse
import com.example.turon.data.model.response.FeedAcceptanceData
import com.example.turon.data.model.response.OrderData
import com.example.turon.data.model.response.ProductAcceptData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.http.Body

class AcceptanceViewModel(private val repository: FeedAcceptanceRepository) : ViewModel() {

    private val _newAcceptState = MutableStateFlow<UIState<List<Acceptance>>>(UIState.Loading)
    val newAcceptState: StateFlow<UIState<List<Acceptance>>> = _newAcceptState
    suspend fun getNewAccept(user_id:Int) {
        _newAcceptState.value = repository.getNewAccept(user_id)
    }



    private val _addStoreState = MutableStateFlow<UIState<EditStoreResponse>>(UIState.Loading)
    val addStoreState: StateFlow<UIState<EditStoreResponse>> = _addStoreState
    suspend fun postAcceptProduct(store_id:Int) {
        _addStoreState.value = repository.postAcceptProduct(store_id)
    }
}