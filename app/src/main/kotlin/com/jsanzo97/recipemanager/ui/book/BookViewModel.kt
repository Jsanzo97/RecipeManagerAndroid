package com.jsanzo97.recipemanager.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsanzo97.common_android.ui.StateHandler
import com.jsanzo97.common_android.ui.StateProvider
import com.jsanzo97.common_android.ui.ViewActionProcessor
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.domain.error.GenericError
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.usecase.book.DeleteAllRecipesFromDb
import com.jsanzo97.domain.usecase.book.DeleteRecipeFromRemoteBookUseCase
import com.jsanzo97.domain.usecase.book.GetBookRecipesFromDbUseCase
import com.jsanzo97.domain.usecase.book.GetBookRecipesFromRemoteUseCase
import com.jsanzo97.recipemanager.R
import kotlinx.coroutines.launch

class BookViewModel(
    private val getBookRecipesFromRemoteUseCase: GetBookRecipesFromRemoteUseCase,
    private val deleteRecipeFromRemoteBookUseCase: DeleteRecipeFromRemoteBookUseCase,
    private val deleteAllRecipesFromDbUseCase: DeleteAllRecipesFromDb,
    private val getBookRecipesFromDbUseCase: GetBookRecipesFromDbUseCase
) : ViewModel(), ViewActionProcessor<BookViewActions>,
        StateProvider<BookViewStates> by StateHandler() {

    private var bookScope = viewModelScope

    override fun processAction(viewAction: BookViewActions) {
        when (viewAction) {
            is BookViewActions.LoadRecipesFromRemote -> loadRecipesFromRemote(viewAction.username, viewAction.shouldShowSpinner)
            is BookViewActions.LoadRecipesFromDb -> loadRecipesFromDb(viewAction.shouldShowSpinner)
            is BookViewActions.DeleteRecipe -> deleteRecipe(viewAction.username, viewAction.recipe)
            is BookViewActions.DeleteAllRecipesFromDb -> deleteAllRecipesFromDb()
        }
    }

    private fun loadRecipesFromRemote(username: String, shouldShowSpinner: Boolean) {
        if (shouldShowSpinner) postState(BookViewStates(loading = true))
        bookScope.launch {
            getBookRecipesFromRemoteUseCase(username).fold(
                {
                    loadRecipesFromDb(shouldShowSpinner)
                },
                { recipeList ->
                    postState(BookViewStates(showRecipes = true, recipeList = recipeList, updatedFromRemote = true))
                }
            )
        }
    }

    private fun loadRecipesFromDb(shouldShowSpinner: Boolean) {
        if (shouldShowSpinner) postState(BookViewStates(loading = true))
        bookScope.launch {
            getBookRecipesFromDbUseCase().fold(
                    { error ->
                        showError(error, R.string.error_loading_recipes_from_server)
                    },
                    { recipeList ->
                        postState(BookViewStates(showRecipes = true, recipeList = recipeList))
                    }
            )
        }
    }

    private fun deleteRecipe(username: String, recipe: Recipe) {
        postState(BookViewStates(loading = true))
        bookScope.launch {
            deleteRecipeFromRemoteBookUseCase(username, recipe).fold(
                {
                    postState(BookViewStates(successDeleteRecipe = true, recipeDeleted = recipe.name))
                },
                { error ->
                    showError(error, R.string.error_removing_recipe_from_server)
                }
            )
        }
    }

    private fun deleteAllRecipesFromDb() {
        postState(BookViewStates(loading = true))
        bookScope.launch {
            deleteAllRecipesFromDbUseCase().fold(
                {
                    postState(BookViewStates(deletedAllDb = true))
                },
                {
                    showError(it, R.string.error_removing_recipe_from_db)
                }
            )
        }
    }

    private fun showError(error: RecipeManagerError, messageId: Int) {
        if (error is GenericError) {
            postState(BookViewStates(genericError = true, genericMessage = error.message))
        } else {
            postState(BookViewStates(specificError = true, specificMessageId = messageId))
        }
    }
}