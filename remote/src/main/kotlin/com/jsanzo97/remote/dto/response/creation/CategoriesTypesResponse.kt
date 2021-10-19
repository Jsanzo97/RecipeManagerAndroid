package com.jsanzo97.remote.dto.response.creation

import com.jsanzo97.data.entity.creation.DataCategoriesTypes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CategoriesTypesResponse(
        @Json(name = "categories")
        val categories: List<DataCategoriesTypes>
)