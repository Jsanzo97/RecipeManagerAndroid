package com.jsanzo97.domain.error

sealed class RecipeManagerError

class GenericError(val message: String = "") : RecipeManagerError()
object AuthenticationError : RecipeManagerError()
object InvalidParametersError : RecipeManagerError()
object OperationError : RecipeManagerError()
object ErrorOnConnection : RecipeManagerError()
object InvalidUserError : RecipeManagerError()
object NotFoundError : RecipeManagerError()
