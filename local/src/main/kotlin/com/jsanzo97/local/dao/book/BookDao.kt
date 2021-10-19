package com.jsanzo97.local.dao.book

import androidx.room.*
import com.jsanzo97.local.entity.book.IngredientEntity
import com.jsanzo97.local.entity.book.RecipeEntity
import com.jsanzo97.local.entity.book.SubcategoryEntity

@Dao
abstract class BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveIngredient(ingredient: IngredientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveSubcategory(subcategory: SubcategoryEntity)

    @Delete
    abstract suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("delete from recipes")
    abstract suspend fun deleteAllRecipes()

    @Query("select * from recipes")
    abstract suspend fun getRecipes(): List<RecipeEntity>

    @Query("select * from ingredients i where i.`recipeName` == :recipeName")
    abstract suspend fun getRecipeIngredients(recipeName: String): List<IngredientEntity>

    @Query("select * from subcategories s where s.`recipeName` == :recipeName")
    abstract suspend fun getRecipeSubcategories(recipeName: String): List<SubcategoryEntity>


}