package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class Datas(
    @SerializedName("activetashkent")
    val activetashkent: List<Activetashkent>,
    @SerializedName("activeviloyat")
    val activeviloyat: List<Activetashkent>

)