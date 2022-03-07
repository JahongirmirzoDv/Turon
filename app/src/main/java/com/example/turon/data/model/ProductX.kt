package com.example.turon.data.model

import com.google.gson.annotations.SerializedName

data class ProductX(
    val id: Int,
    val name: String,
    val pr_code: Int,
    @SerializedName("type")
    val type: TypeXX,
    val weight: Double
)