package com.example.turon.data.api2.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turon.data.api2.ApiHelper2
import com.example.turon.data.model.ChiqmdanQaytaglar
import com.example.turon.data.model.QopChiqim
import com.example.turon.data.model.QopChiqimhistory2
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
    fun getBagHistory(user_id: Int,date_start: String,date_end: String): MutableLiveData<QopHistoryResponse> {
        viewModelScope.launch {
            bagHistory.value = apiHelper2.getBagHistory(user_id,date_start,date_end)
        }
        return bagHistory
    }

    fun returnBag(map: HashMap<String, Any>?): MutableLiveData<ResponseData> {
        var isTrue = MutableLiveData<ResponseData>()
        viewModelScope.launch {
            map?.let { isTrue.value = apiHelper2.returnBag(it) }
        }
        return isTrue
    }

    fun returnExpanceQop(map: HashMap<String, Any>?): MutableLiveData<ResponseData> {
        var isTrue = MutableLiveData<ResponseData>()
        viewModelScope.launch {
            map?.let { isTrue.value = apiHelper2.returnExpanceQop(it) }
        }
        return isTrue
    }

    fun returnIncomeQop(map: HashMap<String, Any>?): MutableLiveData<ResponseData> {
        var isTrue = MutableLiveData<ResponseData>()
        viewModelScope.launch {
            map?.let { isTrue.value = apiHelper2.returnIncomeQop(it) }
        }
        return isTrue
    }

    fun edit(map: HashMap<String, Any>?): MutableLiveData<ResponseData> {
        var isTrue = MutableLiveData<ResponseData>()
        viewModelScope.launch {
            isTrue.value = apiHelper2.Edit(map)
        }
        return isTrue
    }

    fun reject(order_id: Int): MutableLiveData<ResponseData> {
        var isTrue = MutableLiveData<ResponseData>()
        viewModelScope.launch {
            isTrue.value = apiHelper2.reject(order_id)
        }
        return isTrue
    }

    fun chiqimdanQaytarilganlar(user_id: Int): MutableLiveData<ChiqmdanQaytaglar> {
        var isTrue = MutableLiveData<ChiqmdanQaytaglar>()
        viewModelScope.launch {
            isTrue.value = apiHelper2.chiqimdanQaytarilganlar(user_id)
        }
        return isTrue
    }

    fun getreturnedincome(
        user_id: Int,
        date_start: String,
        date_end: String
    ): MutableLiveData<QopChiqimhistory2> {
        var isTrue = MutableLiveData<QopChiqimhistory2>()
        viewModelScope.launch {
            isTrue.value = apiHelper2.getreturnedincome(user_id, date_start, date_end)
        }
        return isTrue
    }

    fun reject_turn(map: HashMap<String, Any>?): MutableLiveData<ResponseData> {
        var isTrue = MutableLiveData<ResponseData>()
        viewModelScope.launch {
            isTrue.value = apiHelper2.reject_turn(map)
        }
        return isTrue
    }

    fun crrete_clinet_tin(map: HashMap<String, Any>?): MutableLiveData<ResponseData> {
        var isTrue = MutableLiveData<ResponseData>()
        viewModelScope.launch {
            isTrue.value = apiHelper2.crrete_clinet_tin(map)
        }
        return isTrue
    }

    fun addTurn(map: HashMap<String, Any>): MutableLiveData<ResponseData> {
        var isTrue = MutableLiveData<ResponseData>()
        viewModelScope.launch {
            isTrue.value = apiHelper2.addTurn(map)
        }
        return isTrue
    }

    fun sendToken(map: HashMap<String, Any>): MutableLiveData<ResponseData> {
        var isTrue = MutableLiveData<ResponseData>()
        viewModelScope.launch {
            isTrue.value = apiHelper2.sendToken(map)
        }
        return isTrue
    }
}