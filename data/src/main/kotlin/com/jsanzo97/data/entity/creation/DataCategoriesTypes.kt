package com.jsanzo97.data.entity.creation

import com.jsanzo97.domain.entity.creation.CategoryType

data class DataCategoriesTypes (
    val category: String
)

fun DataCategoriesTypes.toCategoriesType() = CategoryType(category)