package com.dungtien.amthucbavi.models

data class ViewHistory(
    val id: Long = 0,
    val userId: Long,
    val dishId: Long,
    val viewedAt: String? = null
)
