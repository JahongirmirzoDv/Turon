package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class Activeviloyat(
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
    val turn: Int
)

data class Loadingviloyat(
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
    val turn: Int
)