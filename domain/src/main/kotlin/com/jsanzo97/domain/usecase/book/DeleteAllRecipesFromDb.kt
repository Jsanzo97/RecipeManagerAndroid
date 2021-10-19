package com.jsanzo97.domain.usecase.book

import arrow.core.Option
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.repository.book.BookRepository

class DeleteAllRecipesFromDb(private val bookRepository: BookRepository) {
    suspend operator fun invoke(): Option<RecipeManagerError> = bookRepository.deleteAllRecipesFromDb()
}