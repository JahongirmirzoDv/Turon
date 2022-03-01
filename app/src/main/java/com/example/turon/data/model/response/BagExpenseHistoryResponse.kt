package com.example.turon.data.model.response

import com.example.turon.data.model.BagExpenseHistory

data class BagExpenseHistoryResponse(
    var success: Boolean,
    var qop_history:List<BagExpenseHistory>
    )
