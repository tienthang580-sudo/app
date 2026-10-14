package com.dungtien.amthucbavi.models

data class Dish(
    val id: Long = 0,
    val categoryId: String?,
    val name: String,
    val price: Int,
    val cookingTime: Int = 0,
    val calories: Int = 0,
    val servings: Int = 2,
    val difficulty: String = "Trung bình",
    val imageUrl: String?,
    val description: String?,
    val ingredients: List<String> = emptyList(),
    val steps: List<CookingStep> = emptyList(),
    val viewedAt: String? = null
)
