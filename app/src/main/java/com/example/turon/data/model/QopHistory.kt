package com.example.turon.data.model


import com.google.gson.annotations.SerializedName

data class QopHistory(
    @SerializedName("client")
    val client: Client,
    @SerializedName("date")
    val date: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("izoh")
    val izoh: String?=null,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("type")
    val type: Type
)