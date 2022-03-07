package com.example.turon.data.model

data class OrderHistoryRespone2(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)