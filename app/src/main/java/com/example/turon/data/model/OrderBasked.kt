package com.example.turon.data.model

import com.google.gson.annotations.SerializedName

data class OrderBasked(
    val id: Int,
    @SerializedName("maxsulot")
    val product: String,
    @SerializedName("maxsulot kodi")
    val prCode: Int,
    @SerializedName("qop soni")
    val bagsCount: Float,
    @SerializedName("umumiy miqdori")
    val amount: Double
)
