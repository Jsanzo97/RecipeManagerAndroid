package com.jsanzo97.data.repository.book

import arrow.core.*
import com.jsanzo97.data.datastore.book.CloudBookDataStore
import com.jsanzo97.data.datastore.book.LocalBookDataStore
import com.jsanzo97.data.entity.book.toRecipe
import com.jsanzo97.data.toError
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.repository.book.BookRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BookDataRepository(
        private val cloudBookDataStore: CloudBookDataStore,
        private val localBookDataStore: LocalBookDataStore,
        private val dispatcher: CoroutineDispatcher
) : BookRepository {

    override suspend fun getRecipes(username: String): Either<RecipeManagerError, List<Recipe>> = withContext(dispatcher) {
        cloudBookDataStore.getRecipes(username).fold(
            {
                it.toError().left()
            },
            { dataRecipeList ->
                localBookDataStore.saveRecipesOnDb(dataRecipeList).fold(
                    {
                        dataRecipeList.map{ it.toRecipe() }.right()
                    },
                    {
                        it.toError().left()
                    }
                )
            }
        )
    }

    override suspend fun deleteRecipe(username: String, recipe: Recipe): Option<RecipeManagerError> = withContext(dispatcher) {
        cloudBookDataStore.deleteRecipe(username, recipe.name).fold(
            {
                localBookDataStore.deleteRecipeFromDb(recipe).fold(
                    {
                        None
                    },
                    {
                        it.toError().some()
                    }
                )
            },
            {
                it.toError().some()
            }
        )
    }

    override suspend fun saveRecipeOnDb(recipe: Recipe): Option<RecipeManagerError> = withContext(dispatcher) {
        localBookDataStore.saveRecipeOnDb(recipe).fold(
            {
                None
            },
            {
                it.toError().some()
            }
        )
    }

    override suspend fun saveRecipesOnDb(recipes: List<Recipe>): Option<RecipeManagerError> = withContext(dispatcher) {
        var error: Option<RecipeManagerError> = None
        recipes.forEach { recipe -> localBookDataStore.saveRecipeOnDb(recipe).fold(
            {
                error = None
            },
            {
                error = it.toError().some()
            }
        )}
        error
    }

    override suspend fun deleteRecipeFromDb(recipe: Recipe): Option<RecipeManagerError> = withContext(dispatcher) {
        localBookDataStore.deleteRecipeFromDb(recipe).fold(
            {
                None
            },
            {
                it.toError().some()
            }
        )
    }

    override suspend fun getRecipesFromDb(): Either<RecipeManagerError, List<Recipe>> = withContext(dispatcher) {
        localBookDataStore.getRecipesFromDb().fold(
            {
                it.toError().left()
            },
            {
                it.right()
            }
        )
    }

    override suspend fun deleteAllRecipesFromDb(): Option<RecipeManagerError> = withContext(dispatcher) {
        localBookDataStore.deleteAllRecipesFromDb().fold(
            {
                None
            },
            {
                it.toError().some()
            }
        )
    }
}