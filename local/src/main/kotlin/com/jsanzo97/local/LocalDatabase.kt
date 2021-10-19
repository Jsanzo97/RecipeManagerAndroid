package com.jsanzo97.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsanzo97.local.dao.book.BookDao
import com.jsanzo97.local.entity.book.IngredientEntity
import com.jsanzo97.local.entity.book.RecipeEntity
import com.jsanzo97.local.entity.book.SubcategoryEntity

@Database(
    entities = [
        RecipeEntity::class,
        IngredientEntity::class,
        SubcategoryEntity::class
    ],
    version = 1
)

abstract class LocalDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}


