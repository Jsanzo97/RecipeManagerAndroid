package com.jsanzo97.remote.service.book

import arrow.core.*
import com.jsanzo97.data.datastore.book.CloudBookDataStore
import com.jsanzo97.data.entity.book.DataRecipe
import com.jsanzo97.data.error.CloudDataError
import com.jsanzo97.remote.service.executeNetworkRequest

class BookService(
    private val bookRemoteWebService: BookRemoteWebService
) : CloudBookDataStore {

    override suspend fun getRecipes(username: String): Either<CloudDataError, List<DataRecipe>> {
        return executeNetworkRequest {
            bookRemoteWebService.getRecipes(username)
        }.fold(
                {
                    it.left()
                },
                {
                    it.recipes.right()
                }
        )
    }

    override suspend fun deleteRecipe(username: String, name: String): Option<CloudDataError> {
        return executeNetworkRequest {
            bookRemoteWebService.deleteRecipe(username, name)
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
