package com.jsanzo97.remote.dto.response.book

import com.jsanzo97.data.entity.book.DataRecipe
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BookResponse(
        @Json(name = "recipes")
        val recipes: List<DataRecipe>
)