package com.jsanzo97.data.entity.creation

import com.jsanzo97.domain.entity.creation.SubcategoryType

data class DataSubcategoriesTypes (
    val value: String
)

fun DataSubcategoriesTypes.toSubcategoryType() = SubcategoryType(value)