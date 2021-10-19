package com.jsanzo97.local.entity.book

import androidx.room.Entity
import androidx.room.ForeignKey
import com.jsanzo97.domain.entity.book.Ingredient

@Entity(
    tableName = "ingredients",
    primaryKeys = ["recipeName", "name"],
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = ["name"],
        childColumns = ["recipeName"],
        onDelete = ForeignKey.CASCADE
    )])
data class IngredientEntity(
    val recipeName: String,
    val name: String,
    val type: String,
    val calories: Double
)

fun Ingredient.toIngredientEntity(recipeName: String) = IngredientEntity(
    recipeName,
    name,
    type,
    calories
)

fun IngredientEntity.toIngredient() = Ingredient(
    name,
    type,
    calories
)