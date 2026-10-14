package com.dungtien.amthucbavi.models

data class Category(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val dishCount: Int = 0
)
