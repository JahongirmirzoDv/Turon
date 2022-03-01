package com.example.turon.data.model.response

import com.example.turon.data.model.QopHistory

data class QopHistoryResponse(
    var success: Boolean,
    var qop_history: List<QopHistory>
)
