package com.jsanzo97.local.entity.book

import androidx.room.Entity
import com.jsanzo97.domain.entity.book.Recipe

@Entity(
    tableName = "recipes",
    primaryKeys = ["name"]
)
data class RecipeEntity(

    val name: String,
    val description: String,
    val duration: Double,
    val difficult: String,
    val category: String
)

fun Recipe.toRecipeEntity() = RecipeEntity(
    name,
    description,
    duration,
    difficult,
    category
)