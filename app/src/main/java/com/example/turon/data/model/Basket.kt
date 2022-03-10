package com.example.turon.data.model

import com.google.gson.annotations.SerializedName

data class Basket(
    val brigada: Any,
    val hajmi: Double,
    val id: Int,
    val is_edited: Boolean,
    val load: Boolean,
    val miqdori: Double,
    val price: Double,
    @SerializedName("product")
    val product: ProductN
)