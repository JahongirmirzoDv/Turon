package com.example.turon.data.model

data class Qoblar(
    val id: Int,
    val price: Double,
    val quantity: Int,
    val tegirmon: Tegirmon,
    val type: Type
)