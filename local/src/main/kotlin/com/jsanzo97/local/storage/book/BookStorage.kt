package com.jsanzo97.local.storage.book

import arrow.core.*
import com.jsanzo97.data.datastore.book.LocalBookDataStore
import com.jsanzo97.data.entity.book.DataRecipe
import com.jsanzo97.data.entity.book.toRecipe
import com.jsanzo97.data.error.LocalDataError
import com.jsanzo97.data.error.WritingError
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.local.dao.book.BookDao
import com.jsanzo97.local.entity.book.*

class BookStorage(
    private val bookDao: BookDao
) : LocalBookDataStore {

    override suspend fun saveRecipeOnDb(recipe: Recipe): Option<LocalDataError> {
        return try {
            bookDao.saveRecipe(recipe.toRecipeEntity())
            recipe.ingredients.forEach { bookDao.saveIngredient(it.toIngredientEntity(recipe.name)) }
            recipe.subcategories.forEach { bookDao.saveSubcategory(it.toSubcategoryEntity(recipe.name)) }
            None
        } catch (_: Exception) {
            WritingError.some()
        }
    }

    override suspend fun saveRecipesOnDb(recipes: List<DataRecipe>): Option<LocalDataError> {
        return try {
            recipes.forEach { saveRecipeOnDb(it.toRecipe()) }
            None
        } catch (_: Exception) {
            WritingError.some()
        }
    }

    override suspend fun deleteRecipeFromDb(recipe: Recipe): Option<LocalDataError> {
        return try {
            bookDao.deleteRecipe(recipe.toRecipeEntity())
            None
        } catch (_: Exception) {
            WritingError.some()
        }
    }

    override suspend fun getRecipesFromDb(): Either<LocalDataError, List<Recipe>> {
        return try {
            bookDao.getRecipes().map { recipeEntity ->
                Recipe(
                    recipeEntity.name,
                    recipeEntity.description,
                    recipeEntity.duration,
                    recipeEntity.difficult,
                    bookDao.getRecipeIngredients(recipeEntity.name).map { it.toIngredient() },
                    recipeEntity.category,
                    bookDao.getRecipeSubcategories(recipeEntity.name).map { it.toSubcategory() }
                )
            }.right()
        } catch (_: Exception) {
            WritingError.left()
        }
    }

    override suspend fun deleteAllRecipesFromDb(): Option<LocalDataError> {
        return try {
            bookDao.deleteAllRecipes()
            None
        } catch (_: Exception) {
            WritingError.some()
        }
    }

}