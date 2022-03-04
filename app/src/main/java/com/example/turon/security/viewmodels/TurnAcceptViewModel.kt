package com.example.turon.security.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turon.data.model.ActiveTurn
import com.example.turon.data.model.ClientData
import com.example.turon.data.model.Turn
import com.example.turon.data.model.TurnHistory
import com.example.turon.data.model.repository.FeedSendProductRepository
import com.example.turon.data.model.repository.TurnAcceptRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TurnAcceptViewModel(private val repository: TurnAcceptRepository) : ViewModel() {

    private val _turnState = MutableStateFlow<UIState<List<Turn>>>(UIState.Loading)
    val turnState: StateFlow<UIState<List<Turn>>> = _turnState
    suspend fun getTurnAccept(user_id:Int) {
        _turnState.value = repository.getTurnAccept(user_id)
    }

    private val _turnHistoryState = MutableStateFlow<UIState<TurnHistoryActInAct>>(UIState.Loading)
    val turnHistoryState: StateFlow<UIState<TurnHistoryActInAct>> = _turnHistoryState
    suspend fun getTurnHistory() {
        _turnHistoryState.value = repository.getTurnHistory()
    }



    private val _turnActiveState = MutableStateFlow<UIState<ActiveTurnResponse>>(UIState.Loading)
    val turnActiveState: StateFlow<UIState<ActiveTurnResponse>> = _turnActiveState
    suspend fun getActiveTurn() {
        _turnActiveState.value = repository.getActiveTurn()
    }

    private val _addTurnState = MutableStateFlow<UIState<EditStoreResponse>>(UIState.Loading)
    val addTurnState: StateFlow<UIState<EditStoreResponse>> = _addTurnState
    suspend fun addTurn(body: HashMap<String, Any>?) {
        _addTurnState.value = repository.addTurn(body)
    }

    private val _insertTurn = MutableStateFlow<UIState<EditStoreResponse>>(UIState.Loading)
    val insertTurn: StateFlow<UIState<EditStoreResponse>> = _insertTurn
    suspend fun insertTurns(body: HashMap<String, Any>?) {
        _insertTurn.value = repository.insertTurns(body)
    }

    private val _carLeaveState = MutableStateFlow<UIState<EditStoreResponse>>(UIState.Loading)
    val carLeaveState: StateFlow<UIState<EditStoreResponse>> = _carLeaveState
    suspend fun carLeave(body: Int,status:Int) {
        _carLeaveState.value = repository.carLeave(body,status)
    }



    private val _turnDetailState =
        MutableStateFlow<UIState<List<ClientData>>>(UIState.Loading)
    val turnDetailState: StateFlow<UIState<List<ClientData>>> =
        _turnDetailState
    suspend fun getTurnClient(order_id: Int) {
        _turnDetailState.value = repository.getTurnClient(order_id)
    }


}
