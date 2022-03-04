package com.example.turon.data.api2.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turon.data.api2.ApiHelper2
import com.example.turon.data.model.QopChiqim
import com.example.turon.data.model.ResponseData
import com.example.turon.data.model.response.QopHistoryResponse
import kotlinx.coroutines.launch

class ControlViewModel(var apiHelper2: ApiHelper2) : ViewModel() {
    var data = MutableLiveData<List<QopChiqim>>()
    fun getQop(
        user_id: Int,
        date_start: String,
        date_end: String
    ): MutableLiveData<List<QopChiqim>> {
        viewModelScope.launch {
            data.value = apiHelper2.getQop(user_id, date_start, date_end).qop_chiqim
        }
        return data
    }

    var bagHistory = MutableLiveData<QopHistoryResponse>()
    fun getBagHistory(user_id: Int): MutableLiveData<QopHistoryResponse> {
        viewModelScope.launch {
            bagHistory.value = apiHelper2.getBagHistory(user_id)
        }
        return bagHistory
    }

    fun returnBag(map: HashMap<String, Any>?):MutableLiveData<ResponseData> {
        var isTrue = MutableLiveData<ResponseData>()
        viewModelScope.launch {
            map?.let { isTrue.value = apiHelper2.returnBag(it) }
        }
        return isTrue
    }
}