package com.example.turon.data.model.response

import com.example.turon.data.model.Acceptance

data class AcceptanceResponse(
    val success:Boolean,
    val data:List<Acceptance>

)
