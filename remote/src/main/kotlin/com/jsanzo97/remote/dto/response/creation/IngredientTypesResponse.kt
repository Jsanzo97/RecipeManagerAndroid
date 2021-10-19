package com.jsanzo97.remote.dto.response.creation

import com.jsanzo97.data.entity.creation.DataIngredientTypes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class IngredientTypesResponse(
        @Json(name = "ingredient_types")
        val ingredients: List<DataIngredientTypes>
)