package com.example.turon.data.model.response

import com.example.turon.data.model.ReturnedBasked
import com.example.turon.data.model.ReturnedSec

data class ReturnedBaskedResponse(
    val success:Boolean,
    val data:List<ReturnedBasked>
)
data class ReturnedSecResponse(
    val success:Boolean,
    val data:List<ReturnedSec>
)
