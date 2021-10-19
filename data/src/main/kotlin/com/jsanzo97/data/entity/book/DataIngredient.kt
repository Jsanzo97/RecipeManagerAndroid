package com.jsanzo97.data.entity.book

import com.jsanzo97.domain.entity.book.Ingredient

data class DataIngredient (
    val name: String,
    val type: String,
    val calories: Double
)

fun DataIngredient.toIngredient() = Ingredient (
    name,
    type,
    calories
)

fun Ingredient.toDataIngredient() = DataIngredient(
    name,
    type,
    calories
)