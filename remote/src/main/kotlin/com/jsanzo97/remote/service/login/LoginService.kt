package com.jsanzo97.remote.service.login

import arrow.core.None
import arrow.core.Option
import arrow.core.some
import com.jsanzo97.data.datastore.login.CloudLoginDataStore
import com.jsanzo97.data.error.CloudDataError
import com.jsanzo97.remote.dto.request.login.LoginRequest
import com.jsanzo97.remote.dto.request.login.SignUpRequest
import com.jsanzo97.remote.service.executeNetworkRequest

class LoginService(
    private val loginRemoteWebService: LoginRemoteWebService
) : CloudLoginDataStore {

    override suspend fun login(username: String, password: String): Option<CloudDataError> {
        return executeNetworkRequest {
            loginRemoteWebService.login(
                LoginRequest(
                    username,
                    password
                )
            )
        }.fold(
            {
                it.some()
            },
            {
                None
            }
        )
    }

    override suspend fun signUp(username: String, password: String): Option<CloudDataError> {
        return executeNetworkRequest {
            loginRemoteWebService.signup(
                SignUpRequest(
                    username,
                    password
                )
            )
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