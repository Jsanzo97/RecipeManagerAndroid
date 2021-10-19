package com.jsanzo97.recipemanager.di.login

import com.jsanzo97.domain.usecase.login.LoginUseCase
import com.jsanzo97.domain.usecase.login.SignUpUseCase
import com.jsanzo97.recipemanager.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {

    viewModel { LoginViewModel(get(), get()) }
    factory { LoginUseCase(get()) }
    factory { SignUpUseCase(get()) }
}