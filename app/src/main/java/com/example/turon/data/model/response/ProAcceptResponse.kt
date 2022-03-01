package com.example.turon.data.model.response

import com.google.gson.annotations.SerializedName

data class ProAcceptResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("success")
    val success: Boolean

)
