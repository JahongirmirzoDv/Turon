package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class ProductAcceptResponse(
    @SerializedName("data")
    var `data`: List<ProductAcceptData>,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)

data class Branch(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String
)


data class Client(
    @SerializedName("compony")
    var compony: String?,
    @SerializedName("id")
    var id: Int
)

data class ProductAcceptData(
    @SerializedName("branch")
    var branch: Branch,
    @SerializedName("client")
    var client: Client,
    @SerializedName("date_end")
    var dateEnd: Any?,
    @SerializedName("date_start")
    var dateStart: String?,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String?,
    @SerializedName("stansiya")
    var stansiya: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("total_netto")
    var totalNetto: Int?,
    @SerializedName("vagon_soni")
    var vagonSoni: Int?,
    @SerializedName("is_edited")
    var isEdited: Boolean
)

