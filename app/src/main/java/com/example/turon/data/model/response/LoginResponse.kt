package com.example.turon.data.model.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("success")
    var success: Boolean
)
data class Data(
    @SerializedName("added_date")
    var added_date: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("is_active")
    var is_active: Boolean,
    @SerializedName("name")
    var name: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("type")
    var type: Int,
    @SerializedName("username")
    var username: String
)
