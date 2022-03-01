package com.example.turon.data.model

import com.google.gson.annotations.SerializedName

data class LoadOrder(
    val id: Int,
    @SerializedName("maxsulot")
    val product: String,
    @SerializedName("maxsulot kodi")
    val prCode: Int,
    val status: Boolean,
    @SerializedName("qop soni")
    val bagsCount: Int,
    @SerializedName("umumiy miqdori")
    val amount: Double

)
