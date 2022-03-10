package com.example.turon.data.model

import com.google.gson.annotations.SerializedName

data class Balance(
    @SerializedName("id")
    val id: Int,
    @SerializedName("maxsulot nomi")
    val productName: String,
    @SerializedName("zavod")
    val company: String,
    @SerializedName("qoplar soni")
    val bagCount: Double
) {

    fun balanceString(): String {
        return "${productName}$company${bagCount}"
    }

}

