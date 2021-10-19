package com.jsanzo97.domain.repository.login

import arrow.core.Option
import com.jsanzo97.domain.error.RecipeManagerError

interface LoginRepository {
    suspend fun login(username: String, password: String): Option<RecipeManagerError>
    suspend fun signUp(username: String, password: String): Option<RecipeManagerError>
}