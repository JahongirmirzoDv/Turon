package com.example.turon.security.viewmodels

import androidx.lifecycle.ViewModel
import com.example.turon.data.model.InComeRequest
import com.example.turon.data.model.QopHistory
import com.example.turon.data.model.repository.BagInComeRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.AddBagExpenseResponse
import com.example.turon.data.model.response.TegirmonData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.HashMap

class BagInComeViewModel(private val repository: BagInComeRepository) : ViewModel() {

    private val _providerState = MutableStateFlow<UIState<List<TegirmonData>>>(UIState.Loading)
    val providerState: StateFlow<UIState<List<TegirmonData>>> = _providerState
    suspend fun getProvider(userId: Int) {
        _providerState.value = repository.getProvider(userId)
    }

    private val _addBagInComeState = MutableStateFlow<UIState<AddBagExpenseResponse>>(UIState.Loading)
    val addBagInComeState: StateFlow<UIState<AddBagExpenseResponse>> = _addBagInComeState
    suspend fun addBagInCome(map: HashMap<String, Any>) {
        _addBagInComeState.value = repository.addBagInCome(map)
    }



    private val _typeTinState = MutableStateFlow<UIState<List<TegirmonData>>>(UIState.Loading)
    val typeTinState: StateFlow<UIState<List<TegirmonData>>> = _typeTinState
    suspend fun getTypeTin(userId: Int) {
        _typeTinState.value = repository.getTypeTin(userId)
    }

    private val _filterTinState = MutableStateFlow<UIState<List<QopHistory>>>(UIState.Loading)
    val filterTinState: StateFlow<UIState<List<QopHistory>>> = _filterTinState
    suspend fun getFilterTin(request: InComeRequest) {
        _filterTinState.value = repository.getFilterTin(request)
    }

    private val _bagHistoryState = MutableStateFlow<UIState<List<QopHistory>>>(UIState.Loading)
    val bagHistoryState: StateFlow<UIState<List<QopHistory>>> = _bagHistoryState
    suspend fun getBagHistory(userId: Int) {
        _bagHistoryState.value = repository.getBagHistory(userId)
    }
}