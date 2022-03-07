package com.example.turon.data.model

data class Seller(
    val date_joined: String,
    val email: String,
    val first_name: String,
    val groups: List<Any>,
    val id: Int,
    val is_active: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val last_login: String,
    val last_name: String,
    val order_number: Int,
    val phone: Any,
    val tegirmon: Any,
    val type: Int,
    val user_permissions: List<Any>,
    val username: String
)