package com.jsanzo97.recipemanager.ui.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsanzo97.common_android.ui.StateHandler
import com.jsanzo97.common_android.ui.StateProvider
import com.jsanzo97.common_android.ui.ViewActionProcessor
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.domain.error.GenericError
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.usecase.creation.CreateNewRecipeUseCase
import com.jsanzo97.domain.usecase.creation.GetInformationToCreateRecipeUseCase
import com.jsanzo97.recipemanager.R
import kotlinx.coroutines.launch

class CreationViewModel(
        private val getInformationToCreateRecipeUseCase: GetInformationToCreateRecipeUseCase,
        private val createNewRecipeUseCase: CreateNewRecipeUseCase
) : ViewModel(), ViewActionProcessor<CreationViewAction>,
        StateProvider<CreationViewState> by StateHandler() {

    private var creationScope = viewModelScope

    override fun processAction(viewAction: CreationViewAction) {
        when (viewAction) {
            is CreationViewAction.LoadInformationData -> getInformation()
            is CreationViewAction.CreateNewRecipe -> createNewRecipe(viewAction.username, viewAction.recipe)
        }
    }

    private fun getInformation() {
        postState(
            CreationViewState(loading = true)
        )
        creationScope.launch {
            getInformationToCreateRecipeUseCase().fold(
                {
                    showError(it, R.string.recipe_creation_error_retrieve_information)
                },
                {
                    postState(
                        CreationViewState(
                            loadedTypesInfo = true,
                            ingredientTypes = it.ingredientTypes,
                            difficultTypes = it.difficultTypes,
                            categoryTypes = it.categoriesTypes,
                            subcategoryTypes = it.subcategoriesTypes
                        )
                    )
                }
            )
        }
    }

    private fun createNewRecipe(username: String, recipe: Recipe) {
        postState(
            CreationViewState(loading = true)
        )
        creationScope.launch {
            createNewRecipeUseCase(username, recipe).fold(
                {
                    postState(
                        CreationViewState(
                            successCreationRecipe = true
                        )
                    )
                },
                {
                    showError(it, R.string.recipe_creation_error_creating_recipe)
                }
            )
        }
    }

    private fun showError(error: RecipeManagerError, messageId: Int) {
        if (error is GenericError) {
            postState(CreationViewState(genericError = true, genericMessage = error.message))
        } else {
            postState(CreationViewState(specificError = true, specificMessageId = messageId))
        }
    }
}