package com.example.turon.data.model


import com.google.gson.annotations.SerializedName

data class ReturnedBasked(
    @SerializedName("id")
    val id: Long,
    @SerializedName("maxsulot")
    val maxsulot: String,
    @SerializedName("qopsoni")
    val qopsoni: Long,
    @SerializedName("sana")
    val sana: String,
    @SerializedName("tegirmon")
    val tegirmon: String
)

data class ReturnedSec(
    @SerializedName("id")
    val id: Int,
    @SerializedName("product")
    val product: String,
    @SerializedName("qopsoni")
    val qopsoni: Long,
    @SerializedName("sana")
    val sana: String,
    @SerializedName("izoh")
    val izoh: String,
    @SerializedName("tegirmon")
    val tegirmon: String
)