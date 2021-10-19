package com.jsanzo97.recipemanager.ui.book

interface BookRecipeActionsListener {
    fun deleteRecipe(name: String)
    fun showRecipeDetails(name: String)
}
