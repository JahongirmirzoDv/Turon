package com.example.turon.security.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.turon.data.model.BagExpenseHistory
import com.example.turon.data.model.BagRoom
import com.example.turon.data.model.Qoblar
import com.example.turon.data.model.repository.BagExpenseRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.AddBagExpenseResponse
import com.example.turon.data.model.response.TegirmonData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BagExpenseViewModel(private val repository: BagExpenseRepository) : ViewModel() {
    var _qopQoldiq = MutableLiveData<List<Qoblar>>()

    private val _typeTinState = MutableStateFlow<UIState<List<TegirmonData>>>(UIState.Loading)
    val typeTinState: StateFlow<UIState<List<TegirmonData>>> = _typeTinState
    suspend fun getTypeTin(userId: Int) {
        _typeTinState.value = repository.getTypeTin(userId)
    }

    private val _bagHistoryState =
        MutableStateFlow<UIState<List<BagExpenseHistory>>>(UIState.Loading)
    val bagHistoryState: StateFlow<UIState<List<BagExpenseHistory>>> = _bagHistoryState
    suspend fun getBagHistory(userId: Int) {
        _bagHistoryState.value = repository.getBagHistory(userId)
    }

    private val _addExpenseState = MutableStateFlow<UIState<AddBagExpenseResponse>>(UIState.Loading)
    val addExpenseState: StateFlow<UIState<AddBagExpenseResponse>> = _addExpenseState
    suspend fun addBagExpense(map: HashMap<String, Any>) {
        _addExpenseState.value = repository.addBagExpense(map)
    }

    private val _bagRoomState = MutableStateFlow<UIState<List<BagRoom>>>(UIState.Loading)
    val bagRoomState: StateFlow<UIState<List<BagRoom>>> = _bagRoomState
    suspend fun getBagRoom(userId: Int) {
        _bagRoomState.value = repository.getBagRoom(userId)
    }
}