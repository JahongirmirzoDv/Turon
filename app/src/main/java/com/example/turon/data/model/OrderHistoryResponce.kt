package com.example.turon.data.model

data class OrderHistoryResponce(
    val count: Int,
    val next: String,
    val previous: Any,
    val resultNS: List<ResultN>
)