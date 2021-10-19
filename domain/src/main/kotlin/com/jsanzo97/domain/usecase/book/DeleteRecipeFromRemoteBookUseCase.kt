package com.jsanzo97.domain.usecase.book

import arrow.core.Option
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.repository.book.BookRepository

class DeleteRecipeFromRemoteBookUseCase(private val bookRepository: BookRepository) {

    suspend operator fun invoke(
        username: String,
        recipe: Recipe
    ): Option<RecipeManagerError> = bookRepository.deleteRecipe(username, recipe)
}