package com.example.turon.data.model.response

import com.example.turon.data.model.HistoryProData
import com.example.turon.data.model.OrderHistory

data class OrderHistoryResponse(
    val count:Int?,
    val next:String?,
    val previous:String?=null,
    val results: List<OrderHistory>,

    )
