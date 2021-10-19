package com.jsanzo97.recipemanager.di.main

import com.jsanzo97.common_android.viewmodel.UiConfigurationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    viewModel { UiConfigurationViewModel() }
}
