package com.jsanzo97.remote.dto.request.creation

import com.jsanzo97.data.entity.book.DataIngredient
import com.jsanzo97.data.entity.book.DataSubcategory
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class
CreationRecipeRequest(
        val name: String,
        val description: String,
        val durationInHours: Double,
        val difficult: String,
        val ingredients: List<DataIngredient>,
        val category: String,
        val subcategories: List<DataSubcategory>
)