package com.example.turon.data.model


import com.google.gson.annotations.SerializedName

data class Client(
    @SerializedName("compony")
    val compony: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String
)