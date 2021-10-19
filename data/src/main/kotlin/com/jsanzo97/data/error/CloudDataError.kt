package com.jsanzo97.data.error

sealed class CloudDataError

object InvalidCredentials : CloudDataError()
object InvalidRequest : CloudDataError()
object ErrorPerformingOperation : CloudDataError()
object ConnectionError : CloudDataError()
object ConflictError: CloudDataError()
data class UnrecognizedCloudError(val message: String = "") : CloudDataError()
