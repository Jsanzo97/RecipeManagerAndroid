package com.jsanzo97.recipemanager.ui.creation

import com.jsanzo97.common_android.ui.ViewAction
import com.jsanzo97.domain.entity.book.Recipe

sealed class CreationViewAction : ViewAction {
    object LoadInformationData: CreationViewAction()
    class CreateNewRecipe(val username: String, val recipe: Recipe): CreationViewAction()
}