package com.jsanzo97.domain.entity.book

data class Recipe (
    val name: String,
    val description: String,
    val duration: Double,
    val difficult: String,
    val ingredients: List<Ingredient>,
    val category: String,
    val subcategories: List<Subcategory>
)
