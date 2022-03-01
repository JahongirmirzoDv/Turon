package com.example.turon.data.model

data class InComeRequest(
    var qop_turi: String,
    var kun: String,
    var hafta: String,
    var oy: String,
    var start: String,
    var end: String,
    var user_id: Int,
)
