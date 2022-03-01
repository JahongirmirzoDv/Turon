package com.example.turon.data.model

data class OrderHistory(
    val id: Int,
    val customer:Customer,
    val date:String,
    val car_number:String?=null,
    val driver_phone:String?=null
)
