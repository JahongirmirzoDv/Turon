package com.example.turon.data.model.response

import com.example.turon.data.model.Balance

data class BalanceResponse(
    val success:Boolean,
    val data:List<Balance>
)
