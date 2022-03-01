package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class Tegirmon(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)