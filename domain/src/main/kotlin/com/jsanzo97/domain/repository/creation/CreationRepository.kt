package com.jsanzo97.domain.repository.creation

import arrow.core.Either
import arrow.core.Option
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.domain.entity.creation.CreationTypesInformation
import com.jsanzo97.domain.error.RecipeManagerError

interface CreationRepository {

    suspend fun getCreationTypesInformation(): Either<RecipeManagerError, CreationTypesInformation>
    suspend fun createRecipe(username: String, recipe: Recipe): Option<RecipeManagerError>
}