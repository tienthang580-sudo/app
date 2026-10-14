package com.dungtien.amthucbavi.models

data class User(
    val id: Long = 0,
    val fullName: String,
    val email: String,
    val phone: String?,
    val createdAt: String? = null
)
