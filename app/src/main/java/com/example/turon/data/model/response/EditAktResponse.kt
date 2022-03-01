package com.example.turon.data.model.response

import com.google.gson.annotations.SerializedName

data class EditAktResponse(

    @SerializedName("data")
    var `data`: EditAktData,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("error")
    var error: String
)

data class EditAktData(
    @SerializedName("total")
    var total: AcceptDetailsTotal,

)

data class EditAktTotal(
    @SerializedName("total_brutto")
    var totalBrutto: Double,
    @SerializedName("total_neto")
    var totalNeto: Double,
    @SerializedName("total_tara")
    var totalTara: Double,
    @SerializedName("vagon_soni")
    var vagon_soni: Double
)
