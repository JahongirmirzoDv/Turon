package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class ActiveOrderResponse(
    @SerializedName("data")
    val `data`: List<OrderData>,
    @SerializedName("success")
    val success: Boolean
)