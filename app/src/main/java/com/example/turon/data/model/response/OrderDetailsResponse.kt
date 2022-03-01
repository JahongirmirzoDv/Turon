package com.example.turon.data.model.response


import com.example.turon.data.model.OrderBasked
import com.google.gson.annotations.SerializedName

data class OrderDetailsResponse(
    @SerializedName("data")
    val orderDetailsData: List<OrderBasked>,
    @SerializedName("success")
    val success: Boolean
)