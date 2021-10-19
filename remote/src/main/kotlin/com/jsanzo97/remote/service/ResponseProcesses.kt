package com.jsanzo97.remote.service

import arrow.core.Either
import arrow.core.Option
import arrow.core.left
import arrow.core.rightIfNotNull
import com.jsanzo97.data.error.*
import com.jsanzo97.remote.dto.response.ErrorResponse
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection

private const val INVALID_GRANT_ERROR = "No user was found with this username"

internal suspend fun <T : Any> executeNetworkRequest(f: suspend () -> Response<T>): Either<CloudDataError, T> {
    return Either.catch(f).fold(
        ifLeft = {
            ConnectionError.left()
        }, ifRight = {
            processResponse(it)
        }
    )
}

internal suspend fun <T : Any> processResponse(response: Response<T>): Either<CloudDataError, T> {
    return if (response.isSuccessful) {
        response.body().rightIfNotNull<CloudDataError, T> { UnrecognizedCloudError() }
    } else {
        val error = checkErrorResponse(response.errorBody()).fold(
            {
                when (response.code()) {
                    HttpURLConnection.HTTP_BAD_REQUEST -> InvalidRequest
                    HttpURLConnection.HTTP_UNAUTHORIZED -> InvalidCredentials
                    HttpURLConnection.HTTP_CLIENT_TIMEOUT -> ConnectionError
                    HttpURLConnection.HTTP_CONFLICT -> ConflictError
                    HttpURLConnection.HTTP_INTERNAL_ERROR -> ErrorPerformingOperation
                    else -> UnrecognizedCloudError()
                }
            },
            {
                UnrecognizedCloudError(it.errorDescription)
            }
        )

        error.left()
    }
}

private suspend fun checkErrorResponse(body: ResponseBody?): Option<ErrorResponse> = Either.catch {
    Moshi.Builder().build().adapter(ErrorResponse::class.java).fromJson(body!!.string())!!
}.toOption()

