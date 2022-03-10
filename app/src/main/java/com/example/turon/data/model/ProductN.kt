package com.example.turon.data.model

import com.google.gson.annotations.SerializedName

data class ProductN(
    val id: Int,
    val miqdori: Double,
    val price: Double,
    @SerializedName("product")
    val product: ProductX,
    val tegirmon: Tegirmon
)