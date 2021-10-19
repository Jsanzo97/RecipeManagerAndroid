package com.jsanzo97.data.datastore.book

import arrow.core.Either
import arrow.core.Option
import com.jsanzo97.data.entity.book.DataRecipe
import com.jsanzo97.data.error.CloudDataError

interface CloudBookDataStore {

    suspend fun getRecipes(username: String): Either<CloudDataError, List<DataRecipe>>
    suspend fun deleteRecipe(username: String, name: String): Option<CloudDataError>
}