package com.jsanzo97.data.entity.creation

import com.jsanzo97.domain.entity.creation.IngredientType

data class DataIngredientTypes (
    val type: String
)

fun DataIngredientTypes.toIngredientType() = IngredientType(type)