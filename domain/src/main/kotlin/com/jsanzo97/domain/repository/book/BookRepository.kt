package com.jsanzo97.domain.repository.book

import arrow.core.Either
import arrow.core.Option
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.domain.error.RecipeManagerError

interface BookRepository {
    suspend fun getRecipes(username: String): Either<RecipeManagerError, List<Recipe>>
    suspend fun deleteRecipe(username: String, recipe: Recipe): Option<RecipeManagerError>

    suspend fun saveRecipeOnDb(recipe: Recipe): Option<RecipeManagerError>
    suspend fun saveRecipesOnDb(recipes: List<Recipe>): Option<RecipeManagerError>
    suspend fun deleteRecipeFromDb(recipe: Recipe): Option<RecipeManagerError>
    suspend fun getRecipesFromDb(): Either<RecipeManagerError, List<Recipe>>
    suspend fun deleteAllRecipesFromDb(): Option<RecipeManagerError>
}