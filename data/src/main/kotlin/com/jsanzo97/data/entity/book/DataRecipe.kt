package com.jsanzo97.data.entity.book

import com.jsanzo97.domain.entity.book.Recipe

data class DataRecipe (
        val name: String,
        val description: String,
        val duration: Double,
        val difficult: String,
        val ingredients: List<DataIngredient>,
        val category: String,
        val subcategories: List<DataSubcategory>
)

fun DataRecipe.toRecipe() = Recipe(
    name,
    description,
    duration,
    difficult,
    ingredients.map{ it.toIngredient() },
    category,
    subcategories.map{ it.toSubcategory() }
)

fun Recipe.toDataRecipe() = DataRecipe(
    name,
    description,
    duration,
    difficult,
    ingredients.map { it.toDataIngredient() },
    category,
    subcategories.map { it.toDataSubcategory() }
)