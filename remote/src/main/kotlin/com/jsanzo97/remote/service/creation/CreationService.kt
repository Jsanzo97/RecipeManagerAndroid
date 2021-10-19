package com.jsanzo97.remote.service.creation

import arrow.core.*
import com.jsanzo97.data.datastore.creation.CloudCreationDataStore
import com.jsanzo97.data.entity.book.DataRecipe
import com.jsanzo97.data.entity.creation.DataCategoriesTypes
import com.jsanzo97.data.entity.creation.DataDifficultTypes
import com.jsanzo97.data.entity.creation.DataIngredientTypes
import com.jsanzo97.data.entity.creation.DataSubcategoriesTypes
import com.jsanzo97.data.error.CloudDataError
import com.jsanzo97.remote.dto.request.creation.CreationRecipeRequest
import com.jsanzo97.remote.service.executeNetworkRequest

class CreationService(
        private val creationRemoteWebService: CreationRemoteWebService
) : CloudCreationDataStore {

    override suspend fun getDifficultTypes(): Either<CloudDataError, List<DataDifficultTypes>> {
        return executeNetworkRequest {
            creationRemoteWebService.getDifficulties()
        }.fold(
            {
                it.left()
            },
            {
                it.difficulties.right()
            }
        )
    }

    override suspend fun getCategoriesTypes(): Either<CloudDataError, List<DataCategoriesTypes>> {
        return executeNetworkRequest {
            creationRemoteWebService.getCategories()
        }.fold(
                {
                    it.left()
                },
                {
                    it.categories.right()
                }
        )
    }

    override suspend fun getSubcategoriesTypes(): Either<CloudDataError, List<DataSubcategoriesTypes>> {
        return executeNetworkRequest {
            creationRemoteWebService.getSubcategories()
        }.fold(
                {
                    it.left()
                },
                {
                    it.subcategories.right()
                }
        )
    }

    override suspend fun getIngredientTypes(): Either<CloudDataError, List<DataIngredientTypes>> {
        return executeNetworkRequest {
            creationRemoteWebService.getIngredientTypes()
        }.fold(
                {
                    it.left()
                },
                {
                    it.ingredients.right()
                }
        )
    }

    override suspend fun createRecipe(username: String, recipe: DataRecipe): Option<CloudDataError> {
       return executeNetworkRequest {
           creationRemoteWebService.createRecipe(
               username,
               CreationRecipeRequest(
                   recipe.name,
                   recipe.description,
                   recipe.duration,
                   recipe.difficult,
                   recipe.ingredients,
                   recipe.category, recipe.subcategories
               )
           )
       }.fold(
           {
               it.some()
           },
           {
               None
           }
       )
    }

}