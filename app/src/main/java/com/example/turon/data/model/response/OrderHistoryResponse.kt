package com.example.turon.data.model.response

import com.example.turon.data.model.ResultN

data class OrderHistoryResponse(
    val count:Int?,
    val next:String?,
    val previous:String?=null,
    val results: List<ResultN>,
    )
