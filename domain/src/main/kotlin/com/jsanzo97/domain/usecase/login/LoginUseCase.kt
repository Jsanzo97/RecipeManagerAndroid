package com.jsanzo97.domain.usecase.login

import arrow.core.Option
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.repository.login.LoginRepository

class LoginUseCase(private val loginRepository: LoginRepository) {

    suspend operator fun invoke(
        username: String,
        password: String
    ): Option<RecipeManagerError> = loginRepository.login(username, password)
}
