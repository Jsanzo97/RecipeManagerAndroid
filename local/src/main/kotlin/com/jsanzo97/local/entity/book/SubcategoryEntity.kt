package com.jsanzo97.local.entity.book

import androidx.room.Entity
import androidx.room.ForeignKey
import com.jsanzo97.domain.entity.book.Subcategory

@Entity(
    tableName = "subcategories",
    primaryKeys = ["recipeName", "value"],
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = ["name"],
        childColumns = ["recipeName"],
        onDelete = ForeignKey.CASCADE
    )])
data class SubcategoryEntity(
    val recipeName: String = "",
    val value: String
)

fun Subcategory.toSubcategoryEntity(recipeName: String) = SubcategoryEntity(
    recipeName,
    value
)

fun SubcategoryEntity.toSubcategory() = Subcategory(
    value
)