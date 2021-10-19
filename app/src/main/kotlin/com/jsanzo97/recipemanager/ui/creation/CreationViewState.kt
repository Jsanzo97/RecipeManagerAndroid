package com.jsanzo97.recipemanager.ui.creation

import com.jsanzo97.common_android.ui.ViewState
import com.jsanzo97.domain.entity.creation.CategoryType
import com.jsanzo97.domain.entity.creation.DifficultType
import com.jsanzo97.domain.entity.creation.IngredientType
import com.jsanzo97.domain.entity.creation.SubcategoryType

data class CreationViewState(
        override val genericError: Boolean = false,
        override val genericMessage: String = "",
        override val loading: Boolean = false,
        override val specificError: Boolean = false,
        override val specificMessageId: Int = 0,
        val loadedTypesInfo: Boolean = false,
        val ingredientTypes: List<IngredientType> = listOf(),
        val difficultTypes: List<DifficultType> = listOf(),
        val categoryTypes: List<CategoryType> = listOf(),
        val subcategoryTypes: List<SubcategoryType> = listOf(),
        val successCreationRecipe: Boolean = false
) : ViewState