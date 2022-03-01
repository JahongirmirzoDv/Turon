package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class TurnHistoryActInAct(
    @SerializedName("data")
    val `data`: Datas,
    @SerializedName("success")
    val success: Boolean
)