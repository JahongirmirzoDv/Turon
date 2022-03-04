package com.example.turon.data.model

data class OrderHistoryRespone2(
    val count: Int,
    val next:String?,
    val previous:String?=null,
    val results: List<OrderHistory>
)