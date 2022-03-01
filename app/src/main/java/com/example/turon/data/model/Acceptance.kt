package com.example.turon.data.model

import com.google.gson.annotations.SerializedName

data class Acceptance(
    @SerializedName("id")
    val id: Int,
    @SerializedName("maxsulot_nomi")
    val product: String,
    @SerializedName("sana")
    val sana: String,
    @SerializedName("tegirmon")
    val company: String,
    @SerializedName("qopsoni")
    val bagsCount:Int

)