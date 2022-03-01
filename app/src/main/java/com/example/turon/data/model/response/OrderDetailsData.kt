package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class OrderDetailsData (
    @SerializedName("hajmi")
    val hajmi: Double,
    @SerializedName("id")
    val id: Int,
    @SerializedName("miqdori")
    val miqdori: Double,
    @SerializedName("price")
    val price: Double,
    @SerializedName("product")
    val product: ProductChild
)

data class ProductChild(
    val id: Int,
    val product:ProductSubChild

)

data class ProductSubChild(
    val id: Int,
    val name: String,
    val type: ProductType
)
data class ProductType(
    val id: Int,
    val name: String,
)
