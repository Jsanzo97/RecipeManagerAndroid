package com.jsanzo97.data

import com.jsanzo97.data.error.*
import com.jsanzo97.domain.error.*

/*  ERRORS  */

fun CloudDataError.toError(): RecipeManagerError = when (this) {
    InvalidCredentials -> AuthenticationError
    InvalidRequest -> InvalidParametersError
    ErrorPerformingOperation -> OperationError
    ConnectionError -> ErrorOnConnection
    ConflictError -> InvalidUserError
    is UnrecognizedCloudError -> GenericError(message)
}

fun LocalDataError.toError(): RecipeManagerError = when (this) {
    WritingError -> OperationError
    ReadingError -> OperationError
    IOError -> OperationError
    ElementNotFoundError -> NotFoundError
    InvalidArgumentError -> InvalidParametersError
}