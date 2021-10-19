package com.jsanzo97.domain.usecase.login

import arrow.core.Option
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.repository.login.LoginRepository

class SignUpUseCase(private val signUpRepository: LoginRepository) {

    suspend operator fun invoke(
        username: String,
        password: String
    ): Option<RecipeManagerError> = signUpRepository.signUp(username, password)
}