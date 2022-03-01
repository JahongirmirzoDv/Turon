package com.example.turon.data.model.response


import com.example.turon.data.model.HistoryProData
import com.google.gson.annotations.SerializedName

data class FeedAcceptHistoryResponse(
    @SerializedName("data")
    val `data`: List<HistoryProData>,
    @SerializedName("success")
    val success: Boolean
)



