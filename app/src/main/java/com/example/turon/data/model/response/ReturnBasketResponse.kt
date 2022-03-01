package com.example.turon.data.model.response

import com.example.turon.data.model.ReturnBasket
import com.example.turon.data.model.ReturnedBasked

data class ReturnBasketResponse(
    val success: Boolean,
    val data: List<ReturnBasket>
)