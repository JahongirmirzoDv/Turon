package com.example.turon.data.model.response

import com.example.turon.data.model.Turn
import com.example.turon.data.model.TurnHistory
import com.google.gson.annotations.SerializedName

data class TurnResponse(
    @SerializedName("data")
    val `data`: List<Turn>,
    @SerializedName("success")
    val success: Boolean
)
data class TurnedHistoryResponse(
    @SerializedName("data")
    val `data`: List<TurnHistory>,
    @SerializedName("success")
    val success: Boolean
)