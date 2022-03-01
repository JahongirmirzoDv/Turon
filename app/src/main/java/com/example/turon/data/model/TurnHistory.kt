package com.example.turon.data.model

data class TurnHistory(
    var id:Int,
    var order:TurnOrder?,
    var turn: Int,
    var date: String,

)
data class TurnOrder(
    var id:Int,
    var customer: Customer,
    var car_number:String,
    var driver_phone:String,

)