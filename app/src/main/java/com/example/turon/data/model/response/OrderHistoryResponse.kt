package com.example.turon.data.model.response

import com.example.turon.data.model.Result

data class OrderHistoryResponse(
    val count:Int?,
    val next:String?,
    val previous:String?=null,
    val results: List<Result>,
    )
