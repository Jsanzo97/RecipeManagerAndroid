package com.jsanzo97.remote.service.login

import com.jsanzo97.remote.dto.request.login.LoginRequest
import com.jsanzo97.remote.dto.request.login.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRemoteWebService {

    @POST("logIn")
    suspend fun login(@Body request: LoginRequest): Response<Unit>

    @POST("signUp")
    suspend fun signup(@Body request: SignUpRequest): Response<Unit>
}