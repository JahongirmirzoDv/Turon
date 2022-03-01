package com.example.turon.scales.ui.product_acceptance

import androidx.lifecycle.ViewModel
import com.example.turon.data.model.repository.AcceptDetailsRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.AcceptDetailsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AcceptDetailsViewModel(private val repository: AcceptDetailsRepository) : ViewModel() {

    private val _addWagonState = MutableStateFlow<UIState<AcceptDetailsData>>(UIState.Loading)
    val addWagonState: StateFlow<UIState<AcceptDetailsData>> = _addWagonState
    suspend fun addWagon(body: HashMap<String, Any>?) {
        _addWagonState.value = repository.addWagon(body)
    }


}