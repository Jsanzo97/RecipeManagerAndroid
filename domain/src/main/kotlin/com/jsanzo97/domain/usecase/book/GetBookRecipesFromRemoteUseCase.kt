package com.jsanzo97.domain.usecase.book

import arrow.core.Either
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.repository.book.BookRepository

class GetBookRecipesFromRemoteUseCase(private val bookRepository: BookRepository) {

    suspend operator fun invoke(
            username: String
    ): Either<RecipeManagerError, List<Recipe>> = bookRepository.getRecipes(username)
}