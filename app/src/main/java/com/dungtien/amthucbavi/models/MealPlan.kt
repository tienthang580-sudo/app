package com.dungtien.amthucbavi.models

data class MealPlan(
    val id: Long = 0,
    val userId: Long,
    val dishId: Long,
    val mealType: String,
    val quantity: Int = 1,
    val planDate: String,
    val dishName: String = "",
    val dishPrice: Int = 0,
    val dishCalories: Int = 0,
    val dishImageUrl: String = ""
)
