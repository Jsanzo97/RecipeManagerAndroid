package com.jsanzo97.recipemanager.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsanzo97.common_android.ui.StateHandler
import com.jsanzo97.common_android.ui.StateProvider
import com.jsanzo97.common_android.ui.ViewActionProcessor
import com.jsanzo97.domain.error.GenericError
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.usecase.login.LoginUseCase
import com.jsanzo97.domain.usecase.login.SignUpUseCase
import com.jsanzo97.recipemanager.R
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel(), ViewActionProcessor<LoginViewAction>,
    StateProvider<LoginViewState> by StateHandler() {

    private var loginScope = viewModelScope

    override fun processAction(viewAction: LoginViewAction) {
        when (viewAction) {
            is LoginViewAction.Login -> login(viewAction.user, viewAction.password)
            is LoginViewAction.SignUp -> signUp(viewAction.user, viewAction.password)
        }
    }

    private fun login(user: String, password: String) {
        if (user.isEmpty() || password.isEmpty()) {
            postState(LoginViewState(specificError = true, specificMessageId = R.string.empty_username_or_password))
        } else {
            postState(LoginViewState(loading = true))
            loginScope.launch {
                loginUseCase.invoke(user, password).fold(
                    {
                        postState(LoginViewState(successLogin = true))
                    },
                    { error ->
                        showError(error, R.string.login_log_in)
                    }
                )
            }
        }
    }

    private fun signUp(user: String, password: String) {
        if (user.isEmpty() || password.isEmpty()) {
            postState(LoginViewState(specificError = true, specificMessageId = R.string.empty_username_or_password))
        } else {
            postState(LoginViewState(loading = true))
            loginScope.launch {
                signUpUseCase.invoke(user, password).fold(
                    {
                        postState(LoginViewState(successSignUp = true))
                    },
                    { error ->
                        showError(error, R.string.error_sign_up)
                    }
                )
            }
        }
    }

    private fun showError(error: RecipeManagerError, messageId: Int) {
        if (error is GenericError) {
            postState(LoginViewState(genericError = true, genericMessage = error.message))
        } else {
            postState(LoginViewState(specificError = true, specificMessageId = messageId))
        }
    }
}