package com.example.turon.data.model

import com.google.gson.annotations.SerializedName

data class Turn(
    var id:Int,
    @SerializedName("customer")
    var mijoz:String,
    @SerializedName("date")
    var sana:String

)