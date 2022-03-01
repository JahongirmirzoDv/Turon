package com.example.turon.data.model.response

import com.example.turon.data.model.ClientData
import com.google.gson.annotations.SerializedName

data class TurnDetailsResponse (
    @SerializedName("data")
    val orderDetailsData: List<ClientData>,
    @SerializedName("success")
    val success: Boolean
    )