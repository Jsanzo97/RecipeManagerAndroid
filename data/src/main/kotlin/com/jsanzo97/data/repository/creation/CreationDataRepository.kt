package com.jsanzo97.data.repository.creation

import arrow.core.*
import com.jsanzo97.data.datastore.book.LocalBookDataStore
import com.jsanzo97.data.datastore.creation.CloudCreationDataStore
import com.jsanzo97.data.entity.book.toDataRecipe
import com.jsanzo97.data.entity.creation.*
import com.jsanzo97.data.toError
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.domain.entity.creation.CreationTypesInformation
import com.jsanzo97.domain.error.OperationError
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.repository.creation.CreationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class CreationDataRepository(
        private val creationDataStore: CloudCreationDataStore,
        private val localBookDataStore: LocalBookDataStore,
        private val dispatcher: CoroutineDispatcher
) : CreationRepository {

    override suspend fun getCreationTypesInformation(): Either<RecipeManagerError, CreationTypesInformation> = withContext(dispatcher) {
        var result: Either<RecipeManagerError, CreationTypesInformation> = OperationError.left()

        val creationTypesInformation = CreationTypesInformation(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
        listOf(
            async { creationDataStore.getCategoriesTypes() },
            async { creationDataStore.getDifficultTypes() },
            async { creationDataStore.getSubcategoriesTypes() },
            async { creationDataStore.getIngredientTypes() }
        ).awaitAll().map { coroutinesResult ->
            coroutinesResult.fold(
                {
                    result = it.toError().left()
                },
                { listResult ->
                    listResult.map { element ->
                        when (element) {
                            is DataSubcategoriesTypes -> creationTypesInformation.subcategoriesTypes.add(element.toSubcategoryType())
                            is DataCategoriesTypes -> creationTypesInformation.categoriesTypes.add(element.toCategoriesType())
                            is DataDifficultTypes -> creationTypesInformation.difficultTypes.add(element.toDifficultType())
                            is DataIngredientTypes -> creationTypesInformation.ingredientTypes.add(element.toIngredientType())
                            else -> {}
                        }
                    }
                    result = creationTypesInformation.right()
                }
            )
        }
        result
    }

    override suspend fun createRecipe(username: String, recipe: Recipe): Option<RecipeManagerError> = withContext(dispatcher) {
        creationDataStore.createRecipe(username, recipe.toDataRecipe()).fold(
            {
                localBookDataStore.saveRecipeOnDb(recipe).fold(
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
}