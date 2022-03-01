package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class AcceptDetailsResponse(
    @SerializedName("data")
    var `data`: AcceptDetailsData,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("error")
    var error: String

)

data class AcceptDetailsData(
    @SerializedName("total")
    var total: AcceptDetailsTotal,
    @SerializedName("wagons")
    var wagons: List<AcceptDetailsWagon>
)

data class AcceptDetailsTotal(
    @SerializedName("total_brutto")
    var totalBrutto: Double,
    @SerializedName("total_neto")
    var totalNeto: Double,
    @SerializedName("total_tara")
    var totalTara: Double,
    @SerializedName("vagon_soni")
    var vagon_soni: Int
)

data class AcceptDetailsWagon(
    @SerializedName("brutto_fakt")
    var bruttoFakt: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("netto_fakt")
    var nettoFakt: Int,
    @SerializedName("number")
    var number: Int,
    @SerializedName("tara_fakt")
    var taraFakt: Int
)
