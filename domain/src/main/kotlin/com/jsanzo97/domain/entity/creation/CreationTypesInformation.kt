package com.jsanzo97.domain.entity.creation

data class CreationTypesInformation (
        val difficultTypes: MutableList<DifficultType>,
        val categoriesTypes: MutableList<CategoryType>,
        val subcategoriesTypes: MutableList<SubcategoryType>,
        val ingredientTypes: MutableList<IngredientType>
)