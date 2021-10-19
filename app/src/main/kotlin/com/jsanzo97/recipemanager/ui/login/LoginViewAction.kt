package com.jsanzo97.recipemanager.ui.login

import com.jsanzo97.common_android.ui.ViewAction

sealed class LoginViewAction : ViewAction {
    class Login(val user: String, val password: String) : LoginViewAction()
    class SignUp(val user: String, val password: String) : LoginViewAction()
}