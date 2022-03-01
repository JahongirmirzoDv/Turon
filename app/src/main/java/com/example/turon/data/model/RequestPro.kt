package com.example.turon.data.model

data class RequestPro(
    val bask_id:Int,
    val order_id:Int
)

data class RequestProC(
    val bask_id:Int,
    val order_id:Int,
    val code:List<Long>
)
