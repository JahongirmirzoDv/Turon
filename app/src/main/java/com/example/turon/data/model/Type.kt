package com.example.turon.data.model


import com.google.gson.annotations.SerializedName

data class Type(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)