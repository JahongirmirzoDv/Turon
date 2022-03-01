package com.example.turon.data.model


import com.example.turon.data.model.response.Tegirmon
import com.example.turon.data.model.response.Type

data class HistoryProData(
    val id:Long,
    val product: Product,
    val tegirmon: Tegirmon,
    val date:String,
    val status:String,
    val qopsoni:Int
)

data class Product(
    val id:Long,
    val type: Type,
    val name:String,
    val pr_code:Int
)