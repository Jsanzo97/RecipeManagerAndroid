package com.jsanzo97.remote.dto.response.creation

import com.jsanzo97.data.entity.creation.DataSubcategoriesTypes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SubcategoriesTypesResponse(
        @Json(name = "subcategories")
        val subcategories: List<DataSubcategoriesTypes>
)