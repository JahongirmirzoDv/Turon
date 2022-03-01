package com.example.turon.data.model

import com.example.turon.data.model.response.AcceptDetailsWagon

data class RequestEditScales(

    val akt_id:Int,
    val wagons:List<AcceptDetailsWagon>
)
