package com.example.turon.data.model.response

import com.example.turon.data.model.QopHistory

data class TypeOfTinResponse(
    var success:Boolean,
    var qop_history:List<TegirmonData>
)

data class FilterOfTinResponse(
    var success:Boolean,
    var filtered:List<QopHistory>
)
