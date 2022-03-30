package com.example.turon.data.model

data class ResultN(
    val baskets: List<Basket>,
    val bonus: Int,
    val car_number: String,
    val customer: Customer,
    val date: String,
    var date_time:String,
    val driver_phone: String,
    val entered_date: String,
    val id: Int,
    val img: String,
    val left_date: String,
    val payment_date: String,
    val seller: Seller,
    val status: String,
    val summa_total: String,
    val tegirmon: Int,
    val turned_date: String,
    val type_bonus: Any,
    val update_date: String
)