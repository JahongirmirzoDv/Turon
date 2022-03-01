package com.example.turon.scales.ui.product_acceptance

import androidx.lifecycle.ViewModel
import com.example.turon.data.model.repository.ProductAcceptRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.ProductAcceptData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductAcceptViewModel(private val repository: ProductAcceptRepository) : ViewModel() {

    private val _activeAktState = MutableStateFlow<UIState<List<ProductAcceptData>>>(UIState.Loading)
    val activeAktState: StateFlow<UIState<List<ProductAcceptData>>> = _activeAktState
    suspend fun getActiveAkt() {
        _activeAktState.value = repository.getActiveAkt()
    }

private val _aktHistoryState = MutableStateFlow<UIState<List<ProductAcceptData>>>(UIState.Loading)
    val aktHistoryState: StateFlow<UIState<List<ProductAcceptData>>> = _aktHistoryState
    suspend fun getHistoryAkt() {
        _aktHistoryState.value = repository.getHistoryAkt()
    }


private val _aktHistoryStateFilter = MutableStateFlow<UIState<List<ProductAcceptData>>>(UIState.Loading)
    val aktHistoryStateFilter: StateFlow<UIState<List<ProductAcceptData>>> = _aktHistoryStateFilter
    suspend fun getHistoryAktFilter(date_start: String,date_end: String) {
        _aktHistoryStateFilter.value = repository.getHistoryAktFilter(date_start,date_end)
    }


}