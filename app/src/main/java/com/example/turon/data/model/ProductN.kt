package com.example.turon.data.model

import com.google.gson.annotations.SerializedName

data class ProductN(
    val id: Int,
    val miqdori: Int,
    val price: Double,
    @SerializedName("product")
    val productX: ProductX,
    val tegirmon: Tegirmon
)