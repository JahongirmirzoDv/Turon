package com.example.turon.data.model.response

import com.google.gson.annotations.SerializedName

data class ActiveTurnResponse(
    val success:Boolean,
    val data:LoadingTurn
)

data class LoadingTurn(

    @SerializedName("loadingviloyat")
    val loadingviloyat: List<Activetashkent>,
    @SerializedName("loadingtashkent")
    val loadingtashkent: List<Activetashkent>


)