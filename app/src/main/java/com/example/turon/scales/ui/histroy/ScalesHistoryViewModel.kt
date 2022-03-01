package com.example.turon.scales.ui.histroy

import androidx.lifecycle.ViewModel
import com.example.turon.data.model.RequestEditScales
import com.example.turon.data.model.repository.ScalesHistoryRepository
import com.example.turon.data.model.repository.state.UIState
import com.example.turon.data.model.response.AcceptDetailsData
import com.example.turon.data.model.response.EditAktData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.http.Body


class ScalesHistoryViewModel(private val repository: ScalesHistoryRepository) : ViewModel() {
    private val _aktWagonAllState = MutableStateFlow<UIState<AcceptDetailsData>>(UIState.Loading)
    val aktWagonAllState: StateFlow<UIState<AcceptDetailsData>> = _aktWagonAllState
    suspend fun getAktWagonAll(akt_id: String) {
        _aktWagonAllState.value = repository.getAktWagonAll(akt_id)
    }



    private val _editAktHistoryState = MutableStateFlow<UIState<EditAktData>>(UIState.Loading)
    val editAktHistoryState: StateFlow<UIState<EditAktData>> = _editAktHistoryState
    suspend fun editAktHistory(body: RequestEditScales) {
        _editAktHistoryState.value = repository.editAktHistory(body)
    }


}