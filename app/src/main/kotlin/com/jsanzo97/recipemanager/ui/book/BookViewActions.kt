package com.jsanzo97.recipemanager.ui.book

import com.jsanzo97.common_android.ui.ViewAction
import com.jsanzo97.domain.entity.book.Recipe

sealed class BookViewActions : ViewAction {
    class LoadRecipesFromRemote(val username: String, val shouldShowSpinner: Boolean = true): BookViewActions()
    class LoadRecipesFromDb(val shouldShowSpinner: Boolean = true): BookViewActions()
    class DeleteRecipe(val username: String, val recipe: Recipe): BookViewActions()
    object DeleteAllRecipesFromDb: BookViewActions()
}