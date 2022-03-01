package com.example.turon.data.model.response

import com.example.turon.data.model.LoadOrder

data class LoadOrderResponse(
    val success:Boolean,
    val data:List<LoadOrder>

)
