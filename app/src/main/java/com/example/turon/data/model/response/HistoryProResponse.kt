package com.example.turon.data.model.response


import com.example.turon.data.model.HistoryProData
import com.google.gson.annotations.SerializedName

data class HistoryProResponse(
    val success: Boolean,
    val count:Int?,
    val next:String?,
    val previous:String?=null,
    val results: List<HistoryProData>,
    )