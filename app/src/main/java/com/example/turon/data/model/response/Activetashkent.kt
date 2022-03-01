package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class Activetashkent(
    @SerializedName("car_number")
    val carNumber: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("driver_phone")
    val driverPhone: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("mijoz")
    val mijoz: String,
    @SerializedName("turn")
    val turn: Int,
    @SerializedName("status")
    val status: Int
)