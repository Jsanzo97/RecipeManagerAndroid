package com.jsanzo97.remote.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ErrorResponse(
    @Json(name = "error_description")
    val errorDescription: String
)