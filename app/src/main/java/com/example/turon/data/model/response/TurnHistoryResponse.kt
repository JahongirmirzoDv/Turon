package com.example.turon.data.model.response

import com.example.turon.data.model.TurnHistory
import com.google.gson.annotations.SerializedName

data class TurnHistoryResponse(
    val count:Int,
    val next:String?=null,
    val previous:String?=null,
    val results: List<TurnHistory>,

    )
