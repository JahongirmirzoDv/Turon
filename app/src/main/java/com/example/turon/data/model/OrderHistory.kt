package com.example.turon.data.model

data class OrderHistory(
    val baskets: List<Int>,
    val bonus: Int,
    val car_number: String,
    val customer: Customer,
    val date: String,
    val driver_phone: String,
    val entered_date: Any,
    val id: Int,
    val img: String,
    val left_date: Any,
    val payment_date: String,
    val seller: Int,
    val status: String,
    val summa_total: String,
    val tegirmon: Int,
    val turned_date: String,
    val type_bonus: Any,
    val update_date: String
)