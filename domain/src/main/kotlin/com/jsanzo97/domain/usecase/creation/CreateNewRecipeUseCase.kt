package com.jsanzo97.domain.usecase.creation

import arrow.core.Option
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.repository.creation.CreationRepository

class CreateNewRecipeUseCase(private val creationRepository: CreationRepository) {

    suspend operator fun invoke(username: String, recipe: Recipe): Option<RecipeManagerError> = creationRepository.createRecipe(username, recipe)

}