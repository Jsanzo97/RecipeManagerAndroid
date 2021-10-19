package com.jsanzo97.data.datastore.creation

import arrow.core.Either
import arrow.core.Option
import com.jsanzo97.data.entity.book.DataRecipe
import com.jsanzo97.data.entity.creation.DataCategoriesTypes
import com.jsanzo97.data.entity.creation.DataDifficultTypes
import com.jsanzo97.data.entity.creation.DataIngredientTypes
import com.jsanzo97.data.entity.creation.DataSubcategoriesTypes
import com.jsanzo97.data.error.CloudDataError

interface CloudCreationDataStore {

    suspend fun getDifficultTypes(): Either<CloudDataError, List<DataDifficultTypes>>
    suspend fun getCategoriesTypes(): Either<CloudDataError, List<DataCategoriesTypes>>
    suspend fun getSubcategoriesTypes(): Either<CloudDataError, List<DataSubcategoriesTypes>>
    suspend fun getIngredientTypes(): Either<CloudDataError, List<DataIngredientTypes>>
    suspend fun createRecipe(username: String, recipe: DataRecipe): Option<CloudDataError>

}