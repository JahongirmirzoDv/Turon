package com.example.turon.data.model.response

import com.example.turon.data.model.ProductPro

data class ProductResponse(
    val success: Boolean,
    val data: List<ProductPro>
)
