package com.example.turon.data.model

import com.google.gson.annotations.SerializedName

data class ProductPro(
    val id: Int,
    val name: String,
    @SerializedName("maxsulot codi")
    val prCode: Int
)
