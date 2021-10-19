package com.jsanzo97.recipemanager.ui.book

import com.jsanzo97.common_android.ui.ViewState
import com.jsanzo97.domain.entity.book.Recipe

data class BookViewStates(
        override val genericError: Boolean = false,
        override val genericMessage: String = "",
        override val loading: Boolean = false,
        override val specificError: Boolean = false,
        override val specificMessageId: Int = 0,
        val deletedAllDb: Boolean = false,
        val successDeleteRecipe: Boolean = false,
        val recipeDeleted: String = "",
        val showRecipes: Boolean = false,
        val updatedFromRemote: Boolean = false,
        val recipeList: List<Recipe> = listOf()
) : ViewState