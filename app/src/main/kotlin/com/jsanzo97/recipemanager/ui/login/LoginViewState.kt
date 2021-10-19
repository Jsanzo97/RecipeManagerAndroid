package com.jsanzo97.recipemanager.ui.login

import com.jsanzo97.common_android.ui.ViewState

data class LoginViewState(
        override val genericError: Boolean = false,
        override val genericMessage: String = "",
        override val loading: Boolean = false,
        override val specificError: Boolean = false,
        override val specificMessageId: Int = 0,
        var successLogin: Boolean = false,
        var successSignUp: Boolean = false
) : ViewState
