package com.jsanzo97.data.datastore.book

import arrow.core.Either
import arrow.core.Option
import com.jsanzo97.data.entity.book.DataRecipe
import com.jsanzo97.data.error.LocalDataError
import com.jsanzo97.domain.entity.book.Recipe

interface LocalBookDataStore {

    suspend fun saveRecipeOnDb(recipe: Recipe): Option<LocalDataError>
    suspend fun saveRecipesOnDb(recipes: List<DataRecipe>): Option<LocalDataError>
    suspend fun deleteRecipeFromDb(recipe: Recipe): Option<LocalDataError>
    suspend fun getRecipesFromDb(): Either<LocalDataError, List<Recipe>>
    suspend fun deleteAllRecipesFromDb(): Option<LocalDataError>
}