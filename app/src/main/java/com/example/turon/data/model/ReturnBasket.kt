package com.example.turon.data.model


import com.google.gson.annotations.SerializedName

data class ReturnBasket(
    @SerializedName("hajmi")
    val hajmi: Double,
    @SerializedName("id")
    val id: Int,
    @SerializedName("maxsulot")
    val maxsulot: String,
    @SerializedName("umumiy miqdori")
    val umumiyMiqdori: Double
)