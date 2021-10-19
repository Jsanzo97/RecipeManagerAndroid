package com.jsanzo97.remote.dto.request.login

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SignUpRequest(
    val username: String,
    val password: String
)