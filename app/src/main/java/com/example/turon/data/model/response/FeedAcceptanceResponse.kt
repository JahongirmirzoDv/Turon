package com.example.turon.data.model.response

data class FeedAcceptanceResponse(
    var success:Boolean,
    var data:FeedAcceptanceData

)
data class FeedAcceptanceData(
    var tegirmon:List<TegirmonData>,
    var product:List<ProductData>
)

data class TegirmonData(
    var id:Int,
    var name:String
)

data class ProductData(
    var id: Int,
    var type:TypeData,
    var name: String
)
data class TypeData(
    var id:Int,
    var name:String
)