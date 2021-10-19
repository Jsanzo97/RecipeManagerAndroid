package com.jsanzo97.recipemanager.ui.book

import com.jsanzo97.domain.entity.book.Recipe

data class RecipeRepresentable(
    val name: String = "",
    val category: String = "",
    val duration: String = "",
    val difficult: String = ""
)

fun Recipe.toRecipeRepresentable(): RecipeRepresentable = RecipeRepresentable(
        name,
        category,
        formatDuration(duration),
        difficult
)

fun formatDuration(duration: Double): String {
    return "${duration}h"
}


