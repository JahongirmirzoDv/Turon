package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class OrderData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("sana")
    val date: String,
    @SerializedName("tel nomer")
    val phone: String,
    @SerializedName("mashina nomeri")
    val carNum: String,
    @SerializedName("mijoz")
    val client: String

){

    fun orderString():String{
        return "${id}$date${phone}${client}${carNum}"
    }

}