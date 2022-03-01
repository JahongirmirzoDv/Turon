package com.example.turon.data.model

data class BagExpenseHistory(
    var id:Int,
    var client: Client,
    var type: Type,
    var izoh:String?=null,
    var date:String,
    var quantity:Int
)