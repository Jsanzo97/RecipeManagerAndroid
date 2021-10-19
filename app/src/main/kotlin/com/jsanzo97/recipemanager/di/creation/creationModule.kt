package com.jsanzo97.recipemanager.di.creation

import com.jsanzo97.domain.usecase.creation.CreateNewRecipeUseCase
import com.jsanzo97.domain.usecase.creation.GetInformationToCreateRecipeUseCase
import com.jsanzo97.recipemanager.ui.creation.CreationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val creationModule = module {

    viewModel { CreationViewModel(get(), get()) }
    factory { GetInformationToCreateRecipeUseCase(get()) }
    factory { CreateNewRecipeUseCase(get()) }


}