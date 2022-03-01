package com.example.turon.data.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.turon.data.model.response.OrderData
import com.example.turon.data.model.response.OrderDetailsData

class SharedViewModel: ViewModel() {

    private var orderList = ArrayList<OrderDetailsData>()
    fun sendOrderDetail(arrayList: ArrayList<OrderDetailsData>) {
        orderList = arrayList
    }
    fun getSendOrderDetail(): MutableLiveData<ArrayList<OrderDetailsData>> {
        return MutableLiveData(orderList)
    }

    fun clear(){
        orderList.clear()
    }


    private var mutableLiveDataId: MutableLiveData<String> = MutableLiveData()

    fun setText(s:String){
        mutableLiveDataId.value=s
    }

    fun getText():MutableLiveData<String>{
        return mutableLiveDataId
    }



}