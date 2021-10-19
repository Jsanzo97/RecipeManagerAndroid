package com.jsanzo97.data.repository.login

import arrow.core.None
import arrow.core.Option
import arrow.core.some
import com.jsanzo97.data.datastore.login.CloudLoginDataStore
import com.jsanzo97.data.toError
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.repository.login.LoginRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LoginDataRepository(
    private val cloudLoginDataStore: CloudLoginDataStore,
    private val dispatcher: CoroutineDispatcher
) : LoginRepository {

    override suspend fun login(username: String, password: String): Option<RecipeManagerError> = withContext(dispatcher) {
        cloudLoginDataStore.login(username, password).fold(
            {
                None
            },
            {
                it.toError().some()
            }
        )
    }

    override suspend fun signUp(username: String, password: String): Option<RecipeManagerError> = withContext(dispatcher) {
        cloudLoginDataStore.signUp(username, password).fold(
            {
                None
            },
            {
                it.toError().some()
            }
        )
    }
}