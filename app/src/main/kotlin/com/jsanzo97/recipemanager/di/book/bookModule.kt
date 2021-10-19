package com.jsanzo97.recipemanager.di.book

import com.jsanzo97.domain.usecase.book.DeleteAllRecipesFromDb
import com.jsanzo97.domain.usecase.book.DeleteRecipeFromRemoteBookUseCase
import com.jsanzo97.domain.usecase.book.GetBookRecipesFromDbUseCase
import com.jsanzo97.domain.usecase.book.GetBookRecipesFromRemoteUseCase
import com.jsanzo97.recipemanager.ui.book.BookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bookModule = module {

    viewModel { BookViewModel(get(), get(), get(), get()) }
    factory { GetBookRecipesFromRemoteUseCase(get()) }
    factory { GetBookRecipesFromDbUseCase(get()) }
    factory { DeleteAllRecipesFromDb(get()) }
    factory { DeleteRecipeFromRemoteBookUseCase(get()) }

}